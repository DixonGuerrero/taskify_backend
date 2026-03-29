package com.taskify.taskifyApi.application.service;

import com.taskify.taskifyApi.application.ports.input.ImageServicePort;
import com.taskify.taskifyApi.application.ports.input.RoleServicePort;
import com.taskify.taskifyApi.application.ports.input.UserServicePort;
import com.taskify.taskifyApi.application.ports.output.FileStoragePort;
import com.taskify.taskifyApi.application.ports.output.UserPersistencePort;
import com.taskify.taskifyApi.domain.enums.RoleEnum;
import com.taskify.taskifyApi.domain.exception.user.EmailAlreadyExistsException;
import com.taskify.taskifyApi.domain.exception.user.UserNotFoundException;
import com.taskify.taskifyApi.domain.exception.user.UsernameAlreadyExistsException;
import com.taskify.taskifyApi.domain.model.Image;
import com.taskify.taskifyApi.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static com.taskify.taskifyApi.domain.enums.ImageType.USER;


@Service
@RequiredArgsConstructor
public class UserService implements UserServicePort, UserDetailsService {

    private final UserPersistencePort userPersistencePort;
    private final ImageServicePort imageService;
    private final RoleServicePort roleService;

    @Override
    public User findById(Long id) {
        User user = userPersistencePort.findById(id)
                .orElseThrow(UserNotFoundException::new);

        hydrateImageUser(user);
        return user;
    }


    @Override
    public User findBySessionUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userPersistencePort.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User in session not found: ".concat(username).concat("!")));

        hydrateImageUser(user);
        return user;
    }

    @Override
    public List<User> findAll() {
        List<User> users = userPersistencePort.findAll();
        hydrateImageUser(users);
        return users;
    }


    @Override
    public User save(User user) {

        validateEmail(user.getEmail());
        validateUsername(user.getUsername());

        Image imageSelected = getImageForUserDefault();

        user.setImage(imageSelected);

        applyDefault(user);

        return userPersistencePort.save(user);
    }

    @Override
    public void update(Long id, User user) {
        userPersistencePort.findById(id)
                .ifPresentOrElse(savedUser -> {

                    updateAttributesUser(savedUser, user);

                    userPersistencePort.save(savedUser);

                }, () -> {
                    throw new UserNotFoundException();
                });
    }


    @Override
    public void deleteById(Long id) {

        User userSelected = userPersistencePort.findById(id).orElseThrow(UserNotFoundException::new);

        userPersistencePort.deleteById(userSelected.getId());
    }

    public void validateUsername(String username) {
        if (userPersistencePort.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistsException();
        }
    }

    public void validateEmail(String email) {
        if (userPersistencePort.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException();
        }
    }

    public void validateEmail(String email, Long userId) {
        userPersistencePort.findByEmail(email).ifPresent(
                userSaved -> {
                    if (!userSaved.getId().equals(userId)) {
                        throw new EmailAlreadyExistsException();
                    }
                }
        );
    }

    public void applyDefault(User user) {

        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setIsEnabled(true);
        user.setRole(roleService.findByName(RoleEnum.USER));

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userPersistencePort.findByUsername(username)
                .orElseThrow(
                        () -> new UserNotFoundException("User with username: ".concat(username).concat(" not found."))
                );

        return toUser(user);
    }

    public UserDetails toUser(User user) {

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
                "ROLE_".concat(user.getRole().getName().name())
        );

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getIsEnabled(),
                user.getAccountNonExpired(),
                user.getCredentialsNonExpired(),
                user.getAccountNonLocked(),
                Collections.singleton(authority)
        );
    }


    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    public void updateAttributesUser(User savedUser, User userUpdate) {
        updateIfDifferent(savedUser::setFirstName, savedUser.getFirstName(), userUpdate.getFirstName());
        updateIfDifferent(savedUser::setLastName, savedUser.getLastName(), userUpdate.getLastName());
        updateIfDifferent(savedUser::setUsername, savedUser.getUsername(), userUpdate.getUsername());
        updateIfDifferent(savedUser::setPhone, savedUser.getPhone(), userUpdate.getPhone());

        if (isValidString(userUpdate.getEmail()) && !Objects.equals(savedUser.getEmail(), userUpdate.getEmail())) {
            validateEmail(userUpdate.getEmail(), savedUser.getId());
            savedUser.setEmail(userUpdate.getEmail());
        }

        if (isValidString(userUpdate.getPassword())) {
            String encodedPassword = getPasswordEncoder().encode(userUpdate.getPassword());
            if (!Objects.equals(savedUser.getPassword(), encodedPassword)) {
                savedUser.setPassword(encodedPassword);
            }
        }

        if (userUpdate.getImage() != null && userUpdate.getImage().getId() != null && userUpdate.getImage().getId() != 0L) {
            Long newImageId = userUpdate.getImage().getId();
            Long savedImageId = savedUser.getImage() != null ? savedUser.getImage().getId() : null;
            if (!Objects.equals(savedImageId, newImageId)) {
                Image imageSelected = imageService.findById(newImageId);
                savedUser.setImage(imageSelected);
            }
        }
    }

    private void updateIfDifferent(Consumer<String> setter, String savedValue, String newValue) {
        if (isValidString(newValue) && !Objects.equals(savedValue, newValue)) {
            setter.accept(newValue);
        }
    }

    private boolean isValidString(String value) {
        return value != null && !value.isBlank();
    }

    private Image getImageForUserDefault(){
        return this.imageService.findAllByType(USER).getFirst();
    }

    @Override
    public void hydrateImageUser(List<User> users){
        users.forEach(this::hydrateImageUser);
    }

    @Override
    public void hydrateImageUser(User user) {
        imageService.hydrateImage(user.getImage());
    }

}
