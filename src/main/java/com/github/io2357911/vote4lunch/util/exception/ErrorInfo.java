package com.github.io2357911.vote4lunch.util.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorInfo {
    private final String url;
    private final int status;
    private final String[] details;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ErrorInfo(@JsonProperty("url") CharSequence url, @JsonProperty("status") int status,
                     @JsonProperty("details") String... details) {
        this.url = url.toString();
        this.status = status;
        this.details = details;
    }

    public String getUrl() {
        return url;
    }

    public int getStatus() {
        return status;
    }

    public String[] getDetails() {
        return details;
    }
}