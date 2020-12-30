package com.github.io2357911.vote4lunch.web;

import com.github.io2357911.vote4lunch.to.VoteTo;
import com.github.io2357911.vote4lunch.util.TimeProvider;
import com.github.io2357911.vote4lunch.web.json.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.io2357911.vote4lunch.TestUtil.readFromJson;
import static com.github.io2357911.vote4lunch.TestUtil.userHttpBasic;
import static com.github.io2357911.vote4lunch.UserTestData.user;
import static com.github.io2357911.vote4lunch.VoteTestData.*;
import static com.github.io2357911.vote4lunch.util.VoteUtil.asTo;
import static com.github.io2357911.vote4lunch.util.exception.ErrorType.VALIDATION_ERROR;
import static com.github.io2357911.vote4lunch.web.VoteRestController.MAX_VOTE_TIME;
import static com.github.io2357911.vote4lunch.web.VoteRestController.REST_URL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteRestControllerTest extends AbstractRestControllerTest {

    @Autowired
    private TimeProvider timeProvider;

    @BeforeEach
    void setTime() {
        timeProvider.setTime(VoteRestController.MAX_VOTE_TIME);
    }

    @Test
    void getVotes() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TO_MATCHER.contentJson(asTo(userVote1), asTo(userVote2)));
    }

    @Test
    void getVoteByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/byDate")
                .param("date", "2020-01-30")
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(TO_MATCHER.contentJson(asTo(userVote1)));
    }

    @Test
    void createOrUpdateVote() throws Exception {
        VoteTo newVote = asTo(getNew());
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(user))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newVote)))
                .andDo(print());

        VoteTo created = readFromJson(action, VoteTo.class);
        newVote.setId(created.getId());
        TO_MATCHER.assertMatch(created, newVote);
    }

    @Test
    void createVoteAfterMaxTime() throws Exception {
        timeProvider.setTime(MAX_VOTE_TIME.plusMinutes(1));
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(user))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(asTo(getNew()))))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorInfo(REST_URL, VALIDATION_ERROR,
                        "It's after 11:00. It is too late, vote can't be changed"));
    }
}