package com.github.io2357911.vote4lunch.util;

import com.github.io2357911.vote4lunch.model.Vote;
import com.github.io2357911.vote4lunch.to.VoteTo;
import com.github.io2357911.vote4lunch.util.exception.NotFoundException;

import java.util.Optional;

public class VoteUtil {
    public static VoteTo asTo(Vote vote) {
        return new VoteTo(vote.getId(), vote.getUser().getId(), vote.getRestaurant().getId(), vote.getCreated());
    }

    public static VoteTo asTo(Optional<Vote> vote) {
        return asTo(vote.orElseThrow(() -> new NotFoundException("Vote not found")));
    }
}
