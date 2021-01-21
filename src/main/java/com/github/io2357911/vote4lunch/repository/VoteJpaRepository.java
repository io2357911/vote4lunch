package com.github.io2357911.vote4lunch.repository;

import com.github.io2357911.vote4lunch.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface VoteJpaRepository extends JpaRepository<Vote, Integer> {
    @Query("SELECT v FROM Vote v WHERE v.created=:created")
    List<Vote> getByCreated(@Param("created") LocalDate created);

    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId AND v.created=:created")
    Vote getByUserAndCreated(@Param("userId") int userId, @Param("created") LocalDate created);
}
