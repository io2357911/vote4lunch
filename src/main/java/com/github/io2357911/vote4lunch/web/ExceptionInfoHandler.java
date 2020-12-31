package com.github.io2357911.vote4lunch.web;

import com.github.io2357911.vote4lunch.util.ValidationUtil;
import com.github.io2357911.vote4lunch.util.exception.ErrorInfo;
import com.github.io2357911.vote4lunch.util.exception.ErrorType;
import com.github.io2357911.vote4lunch.util.exception.VoteCantBeChangedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

import static com.github.io2357911.vote4lunch.util.exception.ErrorType.*;

@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {
    private static Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    @ExceptionHandler(VoteCantBeChangedException.class)
    public ResponseEntity<ErrorInfo> voteCantBeChangedError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, VALIDATION_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorInfo> accessDeniedError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, FORBIDDEN_ERROR);
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
