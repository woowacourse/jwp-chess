package chess.controller;

import chess.dto.CommonDto;
import chess.exception.HandledException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(HandledException.class)
    public ResponseEntity<CommonDto<Object>> badRequest(HandledException e) {
        return ResponseEntity.badRequest()
                .body(new CommonDto<>(e.getMessage()));
    }

//    @ExceptionHandler(HandledException.class)
//    public ResponseEntity<String> badRequest(HandledException e) {
//        return ResponseEntity.badRequest()
//                .body(e.getMessage());
//    }
}
