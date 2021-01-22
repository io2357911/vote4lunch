package com.github.io2357911.vote4lunch;

import com.github.io2357911.vote4lunch.model.Vote;
import com.github.io2357911.vote4lunch.to.VoteTo;

import java.time.LocalDate;

import static com.github.io2357911.vote4lunch.RestaurantTestData.restaurant1;
import static com.github.io2357911.vote4lunch.RestaurantTestData.restaurant2;
import static com.github.io2357911.vote4lunch.UserTestData.admin;
import static com.github.io2357911.vote4lunch.UserTestData.user;
import static com.github.io2357911.vote4lunch.model.AbstractBaseEntity.START_SEQ;

public class VoteTestData {
    public static final TestMatcher<Vote> MATCHER = TestMatcher.usingIgnoringFieldsComparator(Vote.class, "user", "restaurant");
    public static final TestMatcher<VoteTo> TO_MATCHER = TestMatcher.usingIgnoringFieldsComparator(VoteTo.class);

    public static final int USER_VOTE1_ID = START_SEQ + 7;
    public static final int USER_VOTE2_ID = START_SEQ + 8;
    public static final int ADMIN_VOTE1_ID = START_SEQ + 9;
    public static final int NOT_FOUND = 2;

    public static final Vote userVote1 = new Vote(USER_VOTE1_ID, user, restaurant1, LocalDate.now());
    public static final Vote userVote2 = new Vote(USER_VOTE2_ID, user, restaurant2, LocalDate.of(2020, 1, 31));
    public static final Vote adminVote1 = new Vote(ADMIN_VOTE1_ID, admin, restaurant1, LocalDate.now());

    public static Vote getNew() {
        return new Vote(null, admin, restaurant2, LocalDate.now());
    }

    public static Vote getUpdated() {
        Vote updated = new Vote(userVote1);
        updated.setRestaurant(restaurant2);
        return updated;
    }
}
