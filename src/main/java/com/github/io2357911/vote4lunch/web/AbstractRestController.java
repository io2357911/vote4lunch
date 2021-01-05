package com.github.io2357911.vote4lunch.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class AbstractRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    public static final String CACHE_RESTAURANTS = "restaurants";
    public static final String CACHE_RESTAURANTS_WITH_DISHES = "restaurants-with-dishes";

    protected <T> ResponseEntity<T> createResponseEntity(String url, int id, T voteTo) {
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(url + "/{id}")
                .buildAndExpand(id).toUri();
        return ResponseEntity.created(uriOfNewResource).body(voteTo);
    }
}
