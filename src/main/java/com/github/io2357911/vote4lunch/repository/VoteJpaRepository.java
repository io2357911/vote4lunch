package com.github.io2357911.vote4lunch.repository;

import com.github.io2357911.vote4lunch.model.Dish;
import com.github.io2357911.vote4lunch.model.Vote;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteJpaRepository extends BaseRepository<Vote> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Vote v WHERE v.id = :id and v.user.id = :userId")
    int delete(@Param("id") int id, @Param("userId") int userId);

    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId AND v.created=:created")
    Optional<Vote> getByUserAndCreated(@Param("userId") int userId, @Param("created") LocalDate created);

    @Query("SELECT v FROM Vote v WHERE v.id = :id and v.user.id = :userId")
    Optional<Vote> get(@Param("id") int id, @Param("userId") int userId);
}
