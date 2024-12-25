package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final NotFoundException exception) {
        log.error("Не найден объект по указанным в запросе данным", exception);
        return new ErrorResponse(
                "404 OBJECT NOT FOUND",
                exception.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationError(final ConditionsNotMetException exception) {
        log.error(exception.getMessage(), exception);
        return new ErrorResponse(
                "400 BAD REQUEST",
                exception.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleUserAlreadyExists(final UserAlreadyExists exception) {
        log.error("Уже существует другой объект с указанными в запросе ключевыми данными", exception);
        return new ErrorResponse(
                "409 USER ALREADY EXISTS",
                exception.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable exception) {
        log.error(exception.getMessage(), exception);
        return new ErrorResponse(
                "Произошла непредвиденная ошибка.",
                exception.getMessage()
        );
    }
}