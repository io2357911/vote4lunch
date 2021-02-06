package com.github.io2357911.vote4lunch;

import com.github.io2357911.vote4lunch.model.Role;
import com.github.io2357911.vote4lunch.model.User;

import static com.github.io2357911.vote4lunch.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;

    public static final User user = new User(USER_ID, "User", "user@mail.com", "user", Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin", "admin@mail.com", "admin", Role.ADMIN);
}
