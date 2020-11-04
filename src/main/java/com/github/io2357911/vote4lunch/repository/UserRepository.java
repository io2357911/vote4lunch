package com.github.io2357911.vote4lunch.repository;

import com.github.io2357911.vote4lunch.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");

    private final UserJpaRepository crudRepository;

    public UserRepository(UserJpaRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    // null if not found, when updated
    public User save(User user) {
        return crudRepository.save(user);
    }

    // false if not found
    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    // null if not found
    public User get(int id) {
        return crudRepository.findById(id).orElse(null);
    }

    // null if not found
    public User getByEmail(String email) {
        return crudRepository.getByEmail(email);
    }

    public List<User> getAll() {
        return crudRepository.findAll(SORT_NAME_EMAIL);
    }
}