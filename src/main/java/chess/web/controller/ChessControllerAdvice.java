package chess.web.controller;

import java.sql.SQLException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ChessControllerAdvice {

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> sqlException(Exception e) {
        return ResponseEntity.internalServerError().body("예상치 못한 데어터베이스 에러가 발생하였습니다.");
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class,
            UnsupportedOperationException.class})
    public ResponseEntity<String> gameOperateException(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> unCheckedServerException(Exception e) {
        return ResponseEntity.internalServerError().body("예상치 못한 서버 에러가 발생하였습니다.");
    }
}
