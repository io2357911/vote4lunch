package com.github.io2357911.vote4lunch.repository;

import com.github.io2357911.vote4lunch.model.User;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface UserRepository extends BaseRepository<User> {
    User getByEmail(String email);
}
