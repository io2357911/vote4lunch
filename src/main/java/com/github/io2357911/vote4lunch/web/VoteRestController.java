package com.github.io2357911.vote4lunch.web;

import com.github.io2357911.vote4lunch.AuthorizedUser;
import com.github.io2357911.vote4lunch.model.Vote;
import com.github.io2357911.vote4lunch.repository.RestaurantJpaRepository;
import com.github.io2357911.vote4lunch.repository.UserJpaRepository;
import com.github.io2357911.vote4lunch.repository.VoteJpaRepository;
import com.github.io2357911.vote4lunch.to.VoteTo;
import com.github.io2357911.vote4lunch.util.TimeProvider;
import com.github.io2357911.vote4lunch.util.exception.NotFoundException;
import com.github.io2357911.vote4lunch.util.exception.VoteCantBeChangedException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.github.io2357911.vote4lunch.util.Util.nowIfNull;
import static com.github.io2357911.vote4lunch.util.ValidationUtil.checkSingleModification;
import static com.github.io2357911.vote4lunch.util.VoteUtil.asTo;

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
    public VoteTo getVoteByDate(@ApiIgnore @AuthenticationPrincipal AuthorizedUser authUser,
                                @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate created) {
        log.info("getVoteByDate user={}, created={}", authUser, created);
        return asTo(voteRepository.getByUserAndCreated(authUser.getId(), nowIfNull(created)));
    }

    @GetMapping("/{id}")
    public VoteTo getVote(@ApiIgnore @AuthenticationPrincipal AuthorizedUser authUser,
                          @PathVariable int id) {
        log.info("getVote user={}, vote {}", authUser, id);
        return asTo(voteRepository.get(id, authUser.getId()));
    }

    @PostMapping
    public ResponseEntity<VoteTo> createVote(@ApiIgnore @AuthenticationPrincipal AuthorizedUser authUser,
                                             @RequestParam int restaurantId) {
        log.info("createVote user={}, restaurantId={}", authUser, restaurantId);
        Vote vote = new Vote(null, userRepository.getOne(authUser.getId()),
                restaurantRepository.getOne(restaurantId), LocalDate.now());
        Vote created = voteRepository.save(vote);
        return createResponseEntity(REST_URL, created.getId(), asTo(created));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateVote(@ApiIgnore @AuthenticationPrincipal AuthorizedUser authUser,
                           @RequestParam int restaurantId) {
        log.info("updateVote user={}, restaurantId={}", authUser, restaurantId);
        checkVoteCanBeChanged();
        Vote vote = voteRepository.getByUserAndCreated(authUser.getId(), LocalDate.now())
                .orElseThrow(() -> new NotFoundException("Vote not found"));
        vote.setRestaurant(restaurantRepository.getOne(restaurantId));
        voteRepository.save(vote);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVote(@ApiIgnore @AuthenticationPrincipal AuthorizedUser authUser,
                           @PathVariable int id) {
        log.info("deleteVote {}", id);
        checkVoteCanBeChanged();
        checkSingleModification(voteRepository.delete(id, authUser.getId()), "Vote id=" + id + " not found");
    }

    private void checkVoteCanBeChanged() {
        if (timeProvider.getTime().isAfter(MAX_VOTE_TIME)) {
            throw new VoteCantBeChangedException("It's after "
                    + MAX_VOTE_TIME.format(DateTimeFormatter.ofPattern("HH:mm"))
                    + ". It is too late, vote can't be changed");
        }
    }
}
