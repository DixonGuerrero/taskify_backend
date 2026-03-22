package com.taskify.taskifyApi.application.service;

import com.taskify.taskifyApi.application.dto.AccessCredentials;
import com.taskify.taskifyApi.application.dto.AuthenticationResult;
import com.taskify.taskifyApi.application.ports.input.AuthServicePort;
import com.taskify.taskifyApi.application.utils.JwtUtils;
import com.taskify.taskifyApi.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthServicePort {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResult login(AccessCredentials accessCredentials) {

        Authentication authentication = this.authenticate(
                accessCredentials.username(),
                accessCredentials.password()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.createToken(authentication);

        return new AuthenticationResult(
                "User logged successfully",
                accessToken,
                true
        );
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = userService.loadUserByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username or password");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());

    }

    @Override
    public AuthenticationResult signup(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User userSaved = this.userService.save(user);
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_".concat(user.getRole().getName().name()));


        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userSaved.getUsername(),
                userSaved.getPassword(),
                Collections.singleton(authority)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtils.createToken(authentication);

        return new AuthenticationResult(
                "User created successfully",
                token,
                true
        );

    }
}
