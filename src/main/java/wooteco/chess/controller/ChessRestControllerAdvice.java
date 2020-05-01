package wooteco.chess.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import wooteco.chess.domain.game.exception.InvalidTurnException;
import wooteco.chess.domain.piece.exception.NotMovableException;
import wooteco.chess.service.exception.InvalidGameException;

@RestControllerAdvice
public class ChessRestControllerAdvice {
	@ExceptionHandler({InvalidGameException.class, InvalidTurnException.class, NotMovableException.class})
	public ResponseEntity handle(RuntimeException e, Model model) {
		model.addAttribute("message", e.getMessage());
		return ResponseEntity.badRequest().body(model);
	}
}
