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
    @Query("SELECT v FROM Vote v WHERE v.date=:date")
    List<Vote> getByDate(@Param("date") LocalDate date);

    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId AND v.date=:date")
    Vote getByUserAndDate(@Param("userId") int userId, @Param("date") LocalDate date);
}
