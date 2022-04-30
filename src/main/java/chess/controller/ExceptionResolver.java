package chess.controller;import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionResolver {

    private static final String DEFAULT_SERVER_EXCEPTION_MESSAGE = "예상치 못한 문제가 발생하였습니다.";
    private static final String EMPTY_STRING = "";
    private static final Logger logger = LoggerFactory.getLogger(ExceptionResolver.class);

    @ExceptionHandler
    public ResponseEntity<String> handleException(Exception e) {
        logger.error(EMPTY_STRING, e);
        return new ResponseEntity<>(DEFAULT_SERVER_EXCEPTION_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(IllegalArgumentException e) {
        logger.error(EMPTY_STRING, e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
