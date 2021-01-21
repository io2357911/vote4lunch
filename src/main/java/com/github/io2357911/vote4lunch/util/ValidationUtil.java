package com.github.io2357911.vote4lunch.util;

import com.github.io2357911.vote4lunch.HasId;
import com.github.io2357911.vote4lunch.util.exception.IllegalRequestDataException;
import com.github.io2357911.vote4lunch.util.exception.NotFoundException;

import java.util.Optional;

public class ValidationUtil {
    private ValidationUtil() {
    }

    public static void checkNew(HasId entity) {
        if (!entity.isNew()) {
            throw new IllegalArgumentException(entity + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(HasId bean, int id) {
//      conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean + " must be with id=" + id);
        }
    }

    public static <T> T checkNotFoundWithId(Optional<T> optional, String msg) {
        return optional.orElseThrow(() -> new NotFoundException(msg));
    }

    public static <T> T checkNotFoundWithId(Optional<T> optional, int id) {
        return checkNotFoundWithId(optional, "Entity with id=" + id +" not found");
    }

    public static void checkSingleModification(int count, String msg) {
        if (count != 1) {
            throw new NotFoundException(msg);
        }
    }

    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }

    public static String getMessage(Throwable e) {
        return e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getClass().getName();
    }
}