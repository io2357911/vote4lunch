package com.github.io2357911.vote4lunch.web.user;

import com.github.io2357911.vote4lunch.model.User;
import com.github.io2357911.vote4lunch.repository.UserJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import java.util.List;

import static com.github.io2357911.vote4lunch.util.ValidationUtil.*;

@Controller
public class UserController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");

    private final UserJpaRepository repository;

    public UserController(UserJpaRepository repository) {
        this.repository = repository;
    }

    public User create(User user) {
        log.info("create {}", user);
        Assert.notNull(user, "user must not be null");
        checkNew(user);
        return repository.save(user);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }

    public User get(int id) {
        log.info("get {}", id);
        User user = repository.findById(id).orElse(null);
        return checkNotFoundWithId(user, id);
    }

    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        Assert.notNull(email, "email must not be null");
        return checkNotFound(repository.getByEmail(email), "email=" + email);
    }

    public List<User> getAll() {
        log.info("getAll");
        return repository.findAll(SORT_NAME_EMAIL);
    }

    public void update(User user) {
        log.info("update {}", user);
        Assert.notNull(user, "user must not be null");
        checkNotFoundWithId(repository.save(user), user.id());
    }
}
