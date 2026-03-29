package com.taskify.taskifyApi.application.ports.input;

import com.taskify.taskifyApi.domain.model.User;

import java.util.List;

public interface UserServicePort {

    User findById(Long id);
    User findBySessionUser();
    List<User> findAll();
    User save(User user);
    void update(Long id, User user);
    void deleteById(Long id);

    void hydrateImageUser(List<User> users);
    void hydrateImageUser(User user);

}
