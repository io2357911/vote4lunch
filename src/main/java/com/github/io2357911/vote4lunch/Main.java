package com.github.io2357911.vote4lunch;

import com.github.io2357911.vote4lunch.model.Role;
import com.github.io2357911.vote4lunch.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("Hello {}!", new User(0, "Name", "Email", "123", Role.ADMIN));
    }
}
