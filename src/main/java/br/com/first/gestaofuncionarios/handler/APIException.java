package br.com.first.gestaofuncionarios.handler;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;


@Getter
@Log4j2
public class APIException extends RuntimeException {
    private final HttpStatus statusException;
    private final ErrorApiResponse bodyException;

    protected APIException(HttpStatus statusException, String message, String description, Exception e) {
        super(message, e);
        this.statusException = statusException;
        this.bodyException = ErrorApiResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(statusException.value())
                .error(statusException.getReasonPhrase())
                .message(message)
                .description(description != null ? description : getDescription(e))
                .build();
    }

    public static APIException build(HttpStatus statusException, String message) {
        return new APIException(
                statusException,
                message,
                null,
                null);
    }

    public static APIException build(HttpStatus statusException, String message, String description) {
        return new APIException(
                statusException,
                message,
                description,
                null);
    }

    public static APIException build(HttpStatus statusException, String message, Exception e) {
        log.error("APIException: {} - {}", statusException, message, e);
        return new APIException(
                statusException,
                message,
                null,
                e);
    }

    public static APIException build(HttpStatus statusException, String message, String description, Exception e) {
        log.error("APIException {} - {} | Description: {}", statusException, message, description, e);
        return new APIException(
                statusException,
                message,
                description,
                e);
    }

    private static String getMessageCause(Exception e) {
        return e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
    }

    private String getDescription(Exception e) {
        return Optional.ofNullable(e)
                .map(APIException::getMessageCause)
                .orElse(null);
    }

    public ResponseEntity<ErrorApiResponse> buildResponseEntity() {
        return ResponseEntity
                .status(statusException)
                .body(bodyException);
    }
}
