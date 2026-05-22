package com.muciociosan.theproject.infrastructure;

import com.muciociosan.theproject.openapi.generated.model.ErrorApiResponseDto;
import com.muciociosan.theproject.shared.exceptions.ApplicationException;
import com.muciociosan.theproject.shared.exceptions.ResourceConflictException;
import com.muciociosan.theproject.shared.exceptions.ResourceNotFoundException;
import com.muciociosan.theproject.shared.exceptions.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorApiResponseDto> handleApplicationExceptionWith500(final Exception exception) {
        log.error("Uncaught Exception: %s".formatted(exception.getMessage()), exception);
        return ResponseEntity.status(HttpStatusCode.valueOf(500))
                .body(new ErrorApiResponseDto()
                        .code("INTERNAL_SERVER_ERROR")
                        .message("Unexpected exception"));
    }

    @ExceptionHandler(ApplicationException.class)
    ResponseEntity<ErrorApiResponseDto> handleApplicationExceptionWith500(final ApplicationException exception) {
        log.error("Uncaught ApplicationException: %s".formatted(exception.getMessage()), exception);
        return ResponseEntity.status(HttpStatusCode.valueOf(500))
                .body(new ErrorApiResponseDto()
                        .code(exception.getCode())
                        .message(exception.getMessage())
                        .details(extractDetails(exception)));
    }

    @ExceptionHandler(ValidationException.class)
    ResponseEntity<ErrorApiResponseDto> handleApplicationExceptionWith400(final ValidationException validationException) {
        return ResponseEntity.status(HttpStatusCode.valueOf(400))
                .body(new ErrorApiResponseDto()
                        .code(validationException.getCode())
                        .message(validationException.getMessage())
                        .details(extractDetails(validationException)));
    }

    @ExceptionHandler(ResourceConflictException.class)
    ResponseEntity<ErrorApiResponseDto> handleApplicationExceptionWith409(final ResourceConflictException conflictException) {
        return ResponseEntity.status(HttpStatusCode.valueOf(409))
                .body(new ErrorApiResponseDto()
                        .code(conflictException.getCode())
                        .message(conflictException.getMessage())
                        .details(extractDetails(conflictException)));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ErrorApiResponseDto> handleApplicationExceptionWith404(final ResourceNotFoundException notFoudException) {
        return ResponseEntity.status(HttpStatusCode.valueOf(404))
                .body(new ErrorApiResponseDto()
                        .code(notFoudException.getCode())
                        .message(notFoudException.getMessage())
                        .details(extractDetails(notFoudException)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorApiResponseDto> handleApplicationExceptionWith404(final MethodArgumentNotValidException argError) {
        final var details = Arrays.stream(argError.getDetailMessageArguments())
                                .map(Object::toString)
                                .filter(StringUtils::isNotBlank)
                                .collect(Collectors.joining(", "));
        log.error(argError.getMessage());
        return ResponseEntity.status(HttpStatusCode.valueOf(400))
                .body(new ErrorApiResponseDto()
                        .code("VALIDATION")
                        .message(details));
    }

    private Object extractDetails(ApplicationException exception) {
        // Depending on the exception type the value should be tailored
        return null;
    }
}
