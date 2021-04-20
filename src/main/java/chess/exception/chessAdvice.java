package chess.exception;

import java.sql.SQLException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class chessAdvice {
    @ExceptionHandler(PieceMoveException.class)
    public ResponseEntity<String> domainExceptionHandle(PieceMoveException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> sqlExceptionHandle(SQLException e) {
        return ResponseEntity.badRequest().body("sql 에러가 발생했습니다.");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> runtimeExceptionHandle(RuntimeException e) {
        return ResponseEntity.badRequest().body("프로그램 실행 중 문제가 발생햇습니다.");
    }
}
