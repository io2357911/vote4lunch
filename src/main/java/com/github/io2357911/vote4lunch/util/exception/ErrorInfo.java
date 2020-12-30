package com.github.io2357911.vote4lunch.util.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorInfo {
    private final String url;
    private final ErrorType type;
    private final String[] details;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ErrorInfo(@JsonProperty("url") CharSequence url, @JsonProperty("type") ErrorType type,
                     @JsonProperty("details") String... details) {
        this.url = url.toString();
        this.type = type;
        this.details = details;
    }

    public String getUrl() {
        return url;
    }

    public ErrorType getType() {
        return type;
    }

    public String[] getDetails() {
        return details;
    }
}