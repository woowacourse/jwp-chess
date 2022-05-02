package chess.controller;

import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.CommunicationException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({CommunicationException.class, DataAccessException.class})
    public ResponseEntity<String> wrongConfigurationExceptionHandle() {
        return ResponseEntity.internalServerError().body("서버에 문제가 발생했습니다.");
    }
}