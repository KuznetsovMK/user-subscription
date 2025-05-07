package web_rize.user_subscription.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import web_rize.user_subscription.exception.NotFoundException;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(NotFoundException exception) {
        var apiError = new ApiError(exception.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }
}
