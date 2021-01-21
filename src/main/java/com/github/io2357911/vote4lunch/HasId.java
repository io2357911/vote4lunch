package com.github.io2357911.vote4lunch;

import org.springframework.util.Assert;

public interface HasId {
    Integer getId();

    void setId(Integer id);

    default boolean isNew() {
        return getId() == null;
    }

    // doesn't work for hibernate lazy proxy
    default int id() {
        Assert.notNull(getId(), "Entity must has id");
        return getId();
    }
}
