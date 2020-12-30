package com.github.io2357911.vote4lunch.util;

import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class TimeProvider {

    LocalTime time;

    public LocalTime getTime() {
        return time == null ? LocalTime.now() : time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
