package com.github.io2357911.vote4lunch.web;

import com.github.io2357911.vote4lunch.model.Vote;
import com.github.io2357911.vote4lunch.util.exception.NotFoundException;
import org.junit.Test;

import java.util.List;

import static com.github.io2357911.vote4lunch.RestaurantTestData.RESTAURANT1_ID;
import static com.github.io2357911.vote4lunch.UserTestData.USER_ID;
import static com.github.io2357911.vote4lunch.VoteTestData.*;
import static org.junit.Assert.assertThrows;

public class VoteControllerTest extends AbstractControllerTest<VoteController> {

    @Test
    public void create() {
        Vote created = controller.create(getNew(), USER_ID, RESTAURANT1_ID);
        int newId = created.id();
        Vote newVote = getNew();
        newVote.setId(newId);
        MATCHER.assertMatch(created, newVote);
        MATCHER.assertMatch(controller.get(newId, USER_ID, RESTAURANT1_ID), newVote);}

    @Test
    public void delete() {
        controller.delete(USER_VOTE1_ID, USER_ID, RESTAURANT1_ID);
        assertThrows(NotFoundException.class, () -> controller.get(USER_VOTE1_ID, USER_ID, RESTAURANT1_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> controller.delete(NOT_FOUND, USER_ID, RESTAURANT1_ID));
    }

    @Test
    public void get() {
        MATCHER.assertMatch(controller.get(USER_VOTE1_ID, USER_ID, RESTAURANT1_ID), userVote1);
    }

    @Test
    public void getAll() {
        List<Vote> all = controller.getAll(USER_ID, RESTAURANT1_ID);
        MATCHER.assertMatch(all, userVote1);
    }

    @Test
    public void update() {
        Vote updated = getUpdated();
        controller.update(updated, USER_ID, RESTAURANT1_ID);
        MATCHER.assertMatch(controller.get(USER_VOTE1_ID, USER_ID, RESTAURANT1_ID), getUpdated());
    }
}