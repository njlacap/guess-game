package njslcp.project.guess_game.config;

import njslcp.project.guess_game.exception.*;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String REQ_ID = "requestId";
    private static final String TIMESTAMP_KEY = "timestamp";
    private static final String MESSAGE_KEY = "message";
    private static final String STATUS_KEY = "status";
    private static final String UNKNOWN_ERROR_MESSAGE = "Unknown error";

    private static final int INTERNAL_SERVER_ERROR_CODE = 500;
    private static final int BAD_REQUEST_CODE = 400;
    private static final int NOT_FOUND_CODE = 404;
    private static final int ALREADY_REPORTED_CODE = 208;

    public GlobalExceptionHandler() { }

    @ExceptionHandler(GuessGameException.class)
    public ResponseEntity<Map<String, Object>> handleGuessGameException( GuessGameException ex ) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("id", MDC.get(REQ_ID));
        errorResponse.put(TIMESTAMP_KEY, LocalDateTime.now());
        errorResponse.put(MESSAGE_KEY, !Strings.isEmpty(ex.getMessage()) ? ex.getMessage() : UNKNOWN_ERROR_MESSAGE);
        errorResponse.put(STATUS_KEY, INTERNAL_SERVER_ERROR_CODE);

        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException( MethodArgumentNotValidException ex ) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("id", MDC.get(REQ_ID));
        errorResponse.put(TIMESTAMP_KEY, LocalDateTime.now());
        errorResponse.put(MESSAGE_KEY, Arrays.toString(ex.getDetailMessageArguments()));
        errorResponse.put(STATUS_KEY, BAD_REQUEST_CODE);

        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler(NoGamesFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoGamesFoundException( NoGamesFoundException ex ) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("id", MDC.get(REQ_ID));
        errorResponse.put(TIMESTAMP_KEY, LocalDateTime.now());
        errorResponse.put(MESSAGE_KEY, !Strings.isEmpty(ex.getMessage()) ? ex.getMessage() : UNKNOWN_ERROR_MESSAGE);
        errorResponse.put(STATUS_KEY, NOT_FOUND_CODE);

        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler(AlreadyGuessedException.class)
    public ResponseEntity<Map<String, Object>> handleAlreadyGuessedException( AlreadyGuessedException ex ) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("id", MDC.get(REQ_ID));
        errorResponse.put(TIMESTAMP_KEY, LocalDateTime.now());
        errorResponse.put(MESSAGE_KEY, !Strings.isEmpty(ex.getMessage()) ? ex.getMessage() : UNKNOWN_ERROR_MESSAGE);
        errorResponse.put(STATUS_KEY, ALREADY_REPORTED_CODE);

        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler(WrongGuessFormatException.class)
    public ResponseEntity<Map<String, Object>> handleWrongGuessFormatException( WrongGuessFormatException ex ) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("id", MDC.get(REQ_ID));
        errorResponse.put(TIMESTAMP_KEY, LocalDateTime.now());
        errorResponse.put(MESSAGE_KEY, !Strings.isEmpty(ex.getMessage()) ? ex.getMessage() : UNKNOWN_ERROR_MESSAGE);
        errorResponse.put(STATUS_KEY, INTERNAL_SERVER_ERROR_CODE);

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(NoMoreAttemptsException.class)
    public ResponseEntity<Map<String, Object>> handleNoMoreAttemptsException( NoMoreAttemptsException ex ) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("id", MDC.get(REQ_ID));
        errorResponse.put(TIMESTAMP_KEY, LocalDateTime.now());
        errorResponse.put(MESSAGE_KEY, !Strings.isEmpty(ex.getMessage()) ? ex.getMessage() : UNKNOWN_ERROR_MESSAGE);
        errorResponse.put(STATUS_KEY, INTERNAL_SERVER_ERROR_CODE);

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(GameOverException.class)
    public ResponseEntity<Map<String, Object>> handleGameOverException( GameOverException ex ) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("id", MDC.get(REQ_ID));
        errorResponse.put(TIMESTAMP_KEY, LocalDateTime.now());
        errorResponse.put(MESSAGE_KEY, !Strings.isEmpty(ex.getMessage()) ? ex.getMessage() : UNKNOWN_ERROR_MESSAGE);
        errorResponse.put(STATUS_KEY, NOT_FOUND_CODE);

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(NotPlayerTurnException.class)
    public ResponseEntity<Map<String, Object>> handleNotPlayerTurnException( NotPlayerTurnException ex ) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("id", MDC.get(REQ_ID));
        errorResponse.put(TIMESTAMP_KEY, LocalDateTime.now());
        errorResponse.put(MESSAGE_KEY, !Strings.isEmpty(ex.getMessage()) ? ex.getMessage() : UNKNOWN_ERROR_MESSAGE);
        errorResponse.put(STATUS_KEY, BAD_REQUEST_CODE);

        return ResponseEntity.badRequest().body(errorResponse);
    }
}
