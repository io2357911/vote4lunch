package com.github.io2357911.vote4lunch.repository;

import com.github.io2357911.vote4lunch.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    // null if not found, when updated
    public User save(User user) {
        throw new UnsupportedOperationException();
    }

    // false if not found
    public boolean delete(int id) {
        throw new UnsupportedOperationException();
    }

    // null if not found
    public User get(int id) {
        throw new UnsupportedOperationException();
    }

    // null if not found
    public User getByEmail(String email) {
        throw new UnsupportedOperationException();
    }

    public List<User> getAll() {
        throw new UnsupportedOperationException();
    }
}