package chess.controller;

import chess.controller.dto.response.ChessGameErrorResponse;
import java.util.NoSuchElementException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ChessGameControllerAdvice {

	@ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class, NullPointerException.class,
			NoSuchElementException.class})
	public ResponseEntity<ChessGameErrorResponse> handleBusinessException(RuntimeException runtimeException) {
		return ResponseEntity.badRequest().body(ChessGameErrorResponse.from(runtimeException));
	}

	@ExceptionHandler(EmptyResultDataAccessException.class)
	public ResponseEntity<Void> handleNotFoundException() {
		return ResponseEntity.notFound().build();
	}

	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<Void> handleSqlException() {
		return ResponseEntity.internalServerError().build();
	}
}
