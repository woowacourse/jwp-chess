package chess.domain.utils;

import chess.domain.exception.DataException;
import chess.domain.response.Response;
import chess.domain.response.StatusEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<Response> passDetailError(IllegalArgumentException e) {
        final Response response = new Response(StatusEnum.BAD_REQUEST, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataException.class)
    public String passDataExceptionError(DataException e) {
        return "lobby";
    }
}
