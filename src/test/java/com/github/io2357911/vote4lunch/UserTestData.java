package com.github.io2357911.vote4lunch;

import com.github.io2357911.vote4lunch.model.Role;
import com.github.io2357911.vote4lunch.model.User;

import static com.github.io2357911.vote4lunch.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    public static TestMatcher<User> MATCHER = TestMatcher.usingIgnoringFieldsComparator(User.class, "registered", "votes");

    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int NOT_FOUND = 2;

    public static final User user = new User(USER_ID, "User", "user@mail.com", "user", Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin", "admin@mail.com", "admin", Role.ADMIN);

    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", Role.USER);
    }

    public static User getUpdated() {
        User updated = new User(user);
        updated.setEmail("update@gmail.com");
        updated.setName("UpdatedName");
        updated.setPassword("newPass");
        updated.setRole(Role.ADMIN);
        return updated;
    }
}
