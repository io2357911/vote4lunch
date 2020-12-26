package com.github.io2357911.vote4lunch.util;

import java.time.LocalDate;

public class Util {
    public static LocalDate nowIfNull(LocalDate date) {
        return date == null ? LocalDate.now() : date;
    }
}
