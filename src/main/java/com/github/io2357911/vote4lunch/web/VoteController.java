package com.github.io2357911.vote4lunch.web;

import com.github.io2357911.vote4lunch.model.Vote;
import com.github.io2357911.vote4lunch.repository.RestaurantJpaRepository;
import com.github.io2357911.vote4lunch.repository.UserJpaRepository;
import com.github.io2357911.vote4lunch.repository.VoteJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import java.util.List;

import static com.github.io2357911.vote4lunch.util.ValidationUtil.checkNew;
import static com.github.io2357911.vote4lunch.util.ValidationUtil.checkNotFoundWithId;

@Controller
public class VoteController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final VoteJpaRepository repository;
    private final UserJpaRepository userRepository;
    private final RestaurantJpaRepository restaurantRepository;

    public VoteController(VoteJpaRepository repository, UserJpaRepository userRepository, RestaurantJpaRepository restaurantRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public Vote create(Vote vote, int userId, int restaurantId) {
        log.info("create {}", vote);
        Assert.notNull(vote, "vote must not be null");
        checkNew(vote);
        return save(vote, userId, restaurantId);
    }

    private Vote save(Vote vote, int userId, int restaurantId) {
        if (!vote.isNew() && get(vote.getId(), userId, restaurantId) == null) {
            return null;
        }
        vote.setUser(userRepository.getOne(userId));
        vote.setRestaurant(restaurantRepository.getOne(restaurantId));
        return repository.save(vote);
    }

    public void delete(int id, int userId, int restaurantId) {
        log.info("delete {}", id);
        checkNotFoundWithId(repository.delete(id, userId, restaurantId) != 0, id);
    }

    public Vote get(int id, int userId, int restaurantId) {
        log.info("get {}", id);
        Vote vote = repository.findById(id)
                .filter(v -> v.getUser().getId() == userId && v.getRestaurant().getId() == restaurantId)
                .orElse(null);
        return checkNotFoundWithId(vote, id);
    }

    public List<Vote> getAll(int userId, int restaurantId) {
        log.info("getAll");
        return repository.getAll(userId, restaurantId);
    }

    public void update(Vote vote, int userId, int restaurantId) {
        log.info("update {}", vote);
        Assert.notNull(vote, "vote must not be null");
        checkNotFoundWithId(save(vote, userId, restaurantId), vote.id());
    }
}
