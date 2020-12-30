package com.github.io2357911.vote4lunch.web;

import com.github.io2357911.vote4lunch.AuthorizedUser;
import com.github.io2357911.vote4lunch.model.Vote;
import com.github.io2357911.vote4lunch.repository.RestaurantJpaRepository;
import com.github.io2357911.vote4lunch.repository.UserJpaRepository;
import com.github.io2357911.vote4lunch.repository.VoteJpaRepository;
import com.github.io2357911.vote4lunch.to.VoteTo;
import com.github.io2357911.vote4lunch.util.TimeProvider;
import com.github.io2357911.vote4lunch.util.exception.VoteCantBeChangedException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.github.io2357911.vote4lunch.util.Util.nowIfNull;
import static com.github.io2357911.vote4lunch.util.VoteUtil.asTo;
import static com.github.io2357911.vote4lunch.util.VoteUtil.asTos;

@RestController
@RequestMapping(value = VoteRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteRestController extends AbstractRestController {
    static final LocalTime MAX_VOTE_TIME = LocalTime.of(11, 0);

    static final String REST_URL = "/rest/votes";

    private final VoteJpaRepository voteRepository;
    private final UserJpaRepository userRepository;
    private final RestaurantJpaRepository restaurantRepository;

    private final TimeProvider timeProvider;

    public VoteRestController(VoteJpaRepository voteRepository,
                              UserJpaRepository userRepository,
                              RestaurantJpaRepository restaurantRepository,
                              TimeProvider timeProvider) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.timeProvider = timeProvider;
    }

    @GetMapping
    public List<VoteTo> getVotes(@ApiIgnore @AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("getAll user={}", authUser);
        return asTos(voteRepository.getAll(authUser.getId()));
    }

    @GetMapping("/byDate")
    public VoteTo getVoteByDate(@ApiIgnore @AuthenticationPrincipal AuthorizedUser authUser,
                                @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getByDate user={} date={}", authUser, date);
        Vote vote = voteRepository.getByDate(authUser.getId(), nowIfNull(date));
        return asTo(vote);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VoteTo> createOrUpdateVote(@ApiIgnore @AuthenticationPrincipal AuthorizedUser authUser,
                                                     @RequestBody VoteTo voteTo) {
        log.info("create user={}, voteTo={}", authUser, voteTo);

        if (timeProvider.getTime().isAfter(MAX_VOTE_TIME)) {
            throw new VoteCantBeChangedException("It's after "
                    + MAX_VOTE_TIME.format(DateTimeFormatter.ofPattern("HH:mm"))
                    + ". It is too late, vote can't be changed");
        }

        LocalDate date = LocalDate.now();

        Vote vote = voteRepository.getByDate(authUser.getId(), date);
        if (vote == null) {
            vote = new Vote(null, userRepository.getOne(authUser.getId()),
                    restaurantRepository.getOne(voteTo.getRestaurantId()), date);
        } else {
            vote.setRestaurant(restaurantRepository.getOne(voteTo.getRestaurantId()));
        }

        Vote created = voteRepository.save(vote);
        return createResponseEntity(REST_URL, created.getId(), asTo(created));
    }
}
