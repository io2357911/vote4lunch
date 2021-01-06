package com.github.io2357911.vote4lunch.web;

import com.github.io2357911.vote4lunch.util.ValidationUtil;
import com.github.io2357911.vote4lunch.util.exception.ErrorInfo;
import com.github.io2357911.vote4lunch.util.exception.ErrorType;
import com.github.io2357911.vote4lunch.util.exception.VoteCantBeChangedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static com.github.io2357911.vote4lunch.util.exception.ErrorType.*;

@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {
    private static Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    public static final String EXCEPTION_RESTAURANT_DUPLICATE = "Restaurant with the same name already exists";
    public static final String EXCEPTION_DISH_DUPLICATE = "Dish with the same name and date already exists";
    public static final String EXCEPTION_DISH_FK_NOT_FOUND = "Restaurant not found for the dish";
    public static final String EXCEPTION_VOTE_FK_NOT_FOUND = "Restaurant not found for the vote";

    private static final Map<String, String> CONSTRAINTS_MAP = new HashMap<String, String>() {{
        put("restaurants_unique_name_idx", EXCEPTION_RESTAURANT_DUPLICATE);
        put("dishes_unique_restaurant_name_date_idx", EXCEPTION_DISH_DUPLICATE);
        put("table: dishes", EXCEPTION_DISH_FK_NOT_FOUND);
        put("table: votes", EXCEPTION_VOTE_FK_NOT_FOUND);
    }};

    @ExceptionHandler(VoteCantBeChangedException.class)
    public ResponseEntity<ErrorInfo> voteCantBeChangedError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, VALIDATION_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorInfo> accessDeniedError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, FORBIDDEN_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorInfo> illegalArgumentError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, VALIDATION_ERROR);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorInfo> bindValidationError(HttpServletRequest req, BindException e) {
        String[] details = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> String.format("[%s] %s", fe.getField(), fe.getDefaultMessage()))
                .toArray(String[]::new);

        return logAndGetErrorInfo(req, e, true, VALIDATION_ERROR, details);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorInfo> conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        String rootMsg = ValidationUtil.getRootCause(e).getMessage();
        if (rootMsg != null) {
            String lowerCaseMsg = rootMsg.toLowerCase();
            for (Map.Entry<String, String> entry : CONSTRAINTS_MAP.entrySet()) {
                if (lowerCaseMsg.contains(entry.getKey())) {
                    return logAndGetErrorInfo(req, e, true, DATA_ERROR, entry.getValue());
                }
            }
        }
        return logAndGetErrorInfo(req, e, true, DATA_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, APP_ERROR);
    }

    private static ResponseEntity<ErrorInfo> logAndGetErrorInfo(HttpServletRequest req, Exception e,
                                                                boolean logException, ErrorType errorType,
                                                                String... details) {
        Throwable rootCause = ValidationUtil.getRootCause(e);

        if (logException) {
            log.error(errorType + " at request " + req.getRequestURL(), rootCause);
        } else {
            log.warn("{} at request  {}: {}", errorType, req.getRequestURL(), rootCause.toString());
        }

        return ResponseEntity.status(errorType.getStatus())
                .body(new ErrorInfo(req.getRequestURL(), errorType,
                        details.length != 0 ? details : new String[]{ValidationUtil.getMessage(rootCause)})
                );
    }
}
