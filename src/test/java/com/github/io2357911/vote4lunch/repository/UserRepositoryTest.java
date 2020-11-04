package com.github.io2357911.vote4lunch.repository;

import com.github.io2357911.vote4lunch.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@ContextConfiguration({
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    public void getAll() {
        List<User> users = repository.getAll();
        assertEquals(2, users.size());
    }
}