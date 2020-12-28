package com.github.io2357911.vote4lunch.web;

import com.github.io2357911.vote4lunch.AuthorizedUser;
import com.github.io2357911.vote4lunch.model.Vote;
import com.github.io2357911.vote4lunch.repository.RestaurantJpaRepository;
import com.github.io2357911.vote4lunch.repository.UserJpaRepository;
import com.github.io2357911.vote4lunch.repository.VoteJpaRepository;
import com.github.io2357911.vote4lunch.to.VoteTo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static com.github.io2357911.vote4lunch.util.Util.nowIfNull;
import static com.github.io2357911.vote4lunch.util.VoteUtil.asTo;
import static com.github.io2357911.vote4lunch.util.VoteUtil.asTos;

@RestController
@RequestMapping(value = VoteRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteRestController {
    static final String REST_URL = "/rest/votes";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final VoteJpaRepository voteRepository;
    private final UserJpaRepository userRepository;
    private final RestaurantJpaRepository restaurantRepository;

    public VoteRestController(VoteJpaRepository voteRepository, UserJpaRepository userRepository, RestaurantJpaRepository restaurantRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping
    public List<VoteTo> getAll(@AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("getAll user={}", authUser);
        return asTos(voteRepository.getAll(authUser.getId()));
    }

    @GetMapping("/byDate")
    public VoteTo getByDate(@AuthenticationPrincipal AuthorizedUser authUser,
                            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getByDate user={} date={}", authUser, date);
        Vote vote = voteRepository.getByDate(authUser.getId(), nowIfNull(date));
        return asTo(vote);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VoteTo> createWithLocation(@AuthenticationPrincipal AuthorizedUser authUser,
                                                     @RequestBody VoteTo voteTo) {
        log.info("create user={}, voteTo={}", authUser, voteTo);

        Vote newVote = new Vote(null, userRepository.getOne(authUser.getId()),
                restaurantRepository.getOne(voteTo.getRestaurantId()), LocalDate.now());
        VoteTo createdTo = asTo(voteRepository.save(newVote));

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(createdTo.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(createdTo);
    }
}
