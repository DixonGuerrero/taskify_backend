package com.taskify.taskifyApi.infrastructure.output.adapters;

import com.taskify.taskifyApi.application.ports.output.UserPersistencePort;
import com.taskify.taskifyApi.domain.model.User;
import com.taskify.taskifyApi.infrastructure.output.mapper.UserPersistenceMapper;
import com.taskify.taskifyApi.infrastructure.output.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPersistencePort {

    private final UserRepository repository;
    @Qualifier("userPersistenceMapper")
    private final UserPersistenceMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return repository.findById(id).map(mapper::toUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return mapper.toUserList(repository.findAll());
    }


    @Override
    @Transactional
    public User save(User user) {
        return mapper.toUser(
                repository.save(mapper.toUserEntity(user))
        );
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email).map(mapper::toUser);
    }

    @Override
    @Transactional
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username).map(mapper::toUser);
    }
}
