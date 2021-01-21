package com.github.io2357911.vote4lunch.repository;

import com.github.io2357911.vote4lunch.util.ValidationUtil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

// https://stackoverflow.com/questions/42781264/multiple-base-repositories-in-spring-data-jpa
@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T, Integer> {
    default T getExisted(int id) {
        return ValidationUtil.checkNotFoundWithId(findById(id), id);
    }
}