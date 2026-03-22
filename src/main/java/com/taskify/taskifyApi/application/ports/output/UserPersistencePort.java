package com.taskify.taskifyApi.application.ports.output;

import com.taskify.taskifyApi.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserPersistencePort {

    Optional<User> findById(Long id);
    List<User> findAll();
    User save(User user);
    void deleteById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

}
