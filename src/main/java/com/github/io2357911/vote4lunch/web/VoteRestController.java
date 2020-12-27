package com.github.io2357911.vote4lunch.web;

import com.github.io2357911.vote4lunch.AuthorizedUser;
import com.github.io2357911.vote4lunch.model.Vote;
import com.github.io2357911.vote4lunch.repository.VoteJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static com.github.io2357911.vote4lunch.util.Util.nowIfNull;

@RestController
@RequestMapping(value = VoteRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteRestController {
    static final String REST_URL = "/rest/votes";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final VoteJpaRepository voteRepository;

    public VoteRestController(VoteJpaRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @GetMapping
    public List<Vote> getAll(@AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("getAll userId={}", authUser.getId());
        return voteRepository.getAll(authUser.getId());
    }

    @GetMapping("/byDate")
    public Vote getByDate(@AuthenticationPrincipal AuthorizedUser authUser,
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("getByDate userId={} date={}", authUser.getId(), date);
        return voteRepository.getByDate(authUser.getId(), nowIfNull(date));
    }
}
