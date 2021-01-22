package com.github.io2357911.vote4lunch.util;

import com.github.io2357911.vote4lunch.model.Vote;
import com.github.io2357911.vote4lunch.to.VoteTo;
import com.github.io2357911.vote4lunch.util.exception.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VoteUtil {
    public static VoteTo asTo(Vote vote) {
        return new VoteTo(vote.getId(), vote.getUser().getId(), vote.getRestaurant().getId(), vote.getCreated());
    }

    public static VoteTo asTo(Optional<Vote> vote) {
        return asTo(vote.orElseThrow(() -> new NotFoundException("Vote not found")));
    }

    public static List<VoteTo> asTos(List<Vote> votes) {
        return votes.stream()
                .map(VoteUtil::asTo)
                .collect(Collectors.toList());
    }
}
