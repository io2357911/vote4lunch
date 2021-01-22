package com.github.io2357911.vote4lunch.web;

import com.github.io2357911.vote4lunch.model.Vote;
import com.github.io2357911.vote4lunch.repository.VoteRepository;
import com.github.io2357911.vote4lunch.to.VoteTo;
import com.github.io2357911.vote4lunch.util.TimeProvider;
import com.github.io2357911.vote4lunch.util.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static com.github.io2357911.vote4lunch.RestaurantTestData.RESTAURANT2_ID;
import static com.github.io2357911.vote4lunch.TestUtil.readFromJson;
import static com.github.io2357911.vote4lunch.TestUtil.userHttpBasic;
import static com.github.io2357911.vote4lunch.UserTestData.admin;
import static com.github.io2357911.vote4lunch.UserTestData.user;
import static com.github.io2357911.vote4lunch.VoteTestData.*;
import static com.github.io2357911.vote4lunch.util.VoteUtil.asTo;
import static com.github.io2357911.vote4lunch.web.ExceptionInfoHandler.EXCEPTION_VOTE_DUPLICATE;
import static com.github.io2357911.vote4lunch.web.VoteRestController.MAX_VOTE_TIME;
import static com.github.io2357911.vote4lunch.web.VoteRestController.REST_URL;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteRestControllerTest extends AbstractRestControllerTest {

    @Autowired
    private VoteRepository repository;

    @Autowired
    private TimeProvider timeProvider;

    @BeforeEach
    void setTime() {
        timeProvider.setTime(VoteRestController.MAX_VOTE_TIME);
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TO_MATCHER.contentJson(asTo(userVote1)));
    }

    @Test
    void create() throws Exception {
        VoteTo newVote = asTo(getNew());
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .param("restaurantId", String.valueOf(newVote.getRestaurantId())))
                .andDo(print());

        VoteTo created = readFromJson(action, VoteTo.class);
        newVote.setId(created.getId());
        TO_MATCHER.assertMatch(created, newVote);
    }

    @Test
    void createDuplicate() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(user))
                .param("restaurantId", String.valueOf(RESTAURANT2_ID)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(errorInfo(REST_URL, CONFLICT, EXCEPTION_VOTE_DUPLICATE));
    }

    @Test
    void createInvalid() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(user))
                .param("restaurantId", String.valueOf(NOT_FOUND)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(errorInfo(REST_URL, CONFLICT, EXCEPTION_VOTE_DUPLICATE));
    }

    @Test
    void update() throws Exception {
        Vote updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .with(userHttpBasic(user))
                .param("restaurantId", String.valueOf(updated.getRestaurant().getId())))
                .andDo(print())
                .andExpect(status().isNoContent());

        MATCHER.assertMatch(repository.getByUserAndCreated(user.getId(), LocalDate.now()).orElse(null), updated);
    }

    @Test
    void updateAfterMaxTime() throws Exception {
        timeProvider.setTime(MAX_VOTE_TIME.plusMinutes(1));
        perform(MockMvcRequestBuilders.put(REST_URL)
                .with(userHttpBasic(user))
                .param("restaurantId", String.valueOf(RESTAURANT2_ID)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorInfo(REST_URL, UNPROCESSABLE_ENTITY,
                        "It's after 11:00. It is too late, vote can't be changed"));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "/" + USER_VOTE1_ID)
                .with(userHttpBasic(user)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> repository.getExisted(USER_VOTE1_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "/" + NOT_FOUND)
                .with(userHttpBasic(user)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}