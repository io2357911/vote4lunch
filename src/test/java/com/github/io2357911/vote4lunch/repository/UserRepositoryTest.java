package com.github.io2357911.vote4lunch.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@ContextConfiguration({
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    public void getAll() {
        assertThrows(UnsupportedOperationException.class, () -> repository.getAll());
    }
}