package com.github.io2357911.vote4lunch.web;

import com.github.io2357911.vote4lunch.util.ValidationUtil;
import com.github.io2357911.vote4lunch.util.exception.ErrorInfo;
import com.github.io2357911.vote4lunch.util.exception.NotFoundException;
import com.github.io2357911.vote4lunch.util.exception.VoteCantBeChangedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {
    private static Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    public static final String EXCEPTION_RESTAURANT_DUPLICATE = "Restaurant with the same name already exists";
    public static final String EXCEPTION_DISH_DUPLICATE = "Dish with the same name and date already exists";
    public static final String EXCEPTION_DISH_FK_NOT_FOUND = "Restaurant not found for the dish";
    public static final String EXCEPTION_VOTE_DUPLICATE = "Vote already exists today";
    public static final String EXCEPTION_VOTE_FK_NOT_FOUND = "Restaurant not found for the vote";

    private static final Map<String, String> CONSTRAINTS_MAP = new HashMap<String, String>() {{
        put("restaurants_unique_name_idx", EXCEPTION_RESTAURANT_DUPLICATE);
        put("dishes_unique_restaurant_name_created_idx", EXCEPTION_DISH_DUPLICATE);
        put("votes_unique_user_created_idx", EXCEPTION_VOTE_DUPLICATE);
        put("table: dishes", EXCEPTION_DISH_FK_NOT_FOUND);
        put("table: votes", EXCEPTION_VOTE_FK_NOT_FOUND);
    }};

    @ExceptionHandler(VoteCantBeChangedException.class)
    public ResponseEntity<ErrorInfo> voteCantBeChangedError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorInfo> notFoundError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorInfo> accessDeniedError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, FORBIDDEN);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorInfo> illegalArgumentError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorInfo> bindValidationError(HttpServletRequest req, BindException e) {
        String[] details = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> String.format("[%s] %s", fe.getField(), fe.getDefaultMessage()))
                .toArray(String[]::new);

        return logAndGetErrorInfo(req, e, true, UNPROCESSABLE_ENTITY, details);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorInfo> conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        String rootMsg = ValidationUtil.getRootCause(e).getMessage();
        if (rootMsg != null) {
            String lowerCaseMsg = rootMsg.toLowerCase();
            for (Map.Entry<String, String> entry : CONSTRAINTS_MAP.entrySet()) {
                if (lowerCaseMsg.contains(entry.getKey())) {
                    return logAndGetErrorInfo(req, e, true, CONFLICT, entry.getValue());
                }
            }
        }
        return logAndGetErrorInfo(req, e, true, CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, INTERNAL_SERVER_ERROR);
    }

    private static ResponseEntity<ErrorInfo> logAndGetErrorInfo(HttpServletRequest req, Exception e,
                                                                boolean logException, HttpStatus status,
                                                                String... details) {
        Throwable rootCause = ValidationUtil.getRootCause(e);

        if (logException) {
            log.error(status + " at request " + req.getRequestURL(), rootCause);
        } else {
            log.warn("{} at request  {}: {}", status, req.getRequestURL(), rootCause.toString());
        }

        return ResponseEntity.status(status)
                .body(new ErrorInfo(req.getRequestURL(), status.value(),
                        details.length != 0 ? details : new String[]{ValidationUtil.getMessage(rootCause)})
                );
    }
}
