package chess.controller.exception;

import chess.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(NoSuchPermittedChessPieceException.class)
    public ResponseEntity<String> handleNoSuchPermittedChessPieceException(NoSuchPermittedChessPieceException e) {
        return badRequest(e);
    }

    @ExceptionHandler(AlreadyPlayingChessGameException.class)
    public ResponseEntity<String> handleAlreadyPlayingChessGameException(AlreadyPlayingChessGameException e) {
        return badRequest(e);
    }

    @ExceptionHandler(NotFoundPlayingChessGameException.class)
    public ResponseEntity<String> handleNotFoundPlayingChessGameException(NotFoundPlayingChessGameException e) {
        return badRequest(e);
    }

    @ExceptionHandler(NotPermittedChessPosition.class)
    public ResponseEntity<String> handleNotPermittedChessPosition(NotPermittedChessPosition e) {
        return badRequest(e);
    }

    @ExceptionHandler(NotMoveToTargetPosition.class)
    public ResponseEntity<String> handleNotMoveToTargetPosition(NotMoveToTargetPosition e) {
        return badRequest(e);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundChessGameException.class)
    public String handleNotFoundChessGame(NotFoundChessGameException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error-page";
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    private ResponseEntity<String> badRequest(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
