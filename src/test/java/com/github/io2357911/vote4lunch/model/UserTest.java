package com.github.io2357911.vote4lunch.model;

import org.junit.Assert;

public class UserTest {

    static final User admin = new User(0, "Name", "Email", "123", Role.ADMIN);

    @org.junit.Test
    public void getRole() {
        Assert.assertEquals(Role.ADMIN, admin.getRole());
    }
}