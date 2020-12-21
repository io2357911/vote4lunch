package com.github.io2357911.vote4lunch;

import com.github.io2357911.vote4lunch.model.Vote;

import java.time.LocalDate;

import static com.github.io2357911.vote4lunch.model.AbstractBaseEntity.START_SEQ;

public class VoteTestData {
    public static final TestMatcher<Vote> MATCHER = TestMatcher.usingIgnoringFieldsComparator(Vote.class, "user", "restaurant");

    public static final int USER_VOTE1_ID = START_SEQ + 7;
    public static final int USER_VOTE2_ID = START_SEQ + 8;
    public static final int ADMIN_VOTE1_ID = START_SEQ + 9;
    public static final int NOT_FOUND = 2;

    public static final Vote userVote1 = new Vote(USER_VOTE1_ID, LocalDate.of(2020, 1, 30));
    public static final Vote userVote2 = new Vote(USER_VOTE2_ID, LocalDate.of(2020, 1, 31));
    public static final Vote adminVote1 = new Vote(ADMIN_VOTE1_ID, LocalDate.of(2020, 1, 30));

    public static Vote getNew() {
        return new Vote(null, LocalDate.of(2020, 2, 1));
    }

    public static Vote getUpdated() {
        Vote updated = new Vote(userVote1);
        updated.setDate(LocalDate.of(2020, 2, 2));
        return updated;
    }
}
