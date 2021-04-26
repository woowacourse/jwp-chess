package chess.controller;

import chess.service.game.ChessGameService;
import dto.ChessGameDto;
import dto.MoveDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ChessGameController {
    private final ChessGameService chessGameService;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> checkChessGameException(Exception exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler({MissingRequestCookieException.class})
    public ResponseEntity<String> checkCookieMissing(Exception exception) {
        return ResponseEntity.badRequest().body("로그인을 해 주세요.");
    }

    public ChessGameController(final ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @PutMapping("/game/{id}")
    public ResponseEntity<ChessGameDto> movePiece(@CookieValue(value = "user") String cookie, @PathVariable("id") Long gameId, @RequestBody MoveDto moveDto) {
        return ResponseEntity.ok().body(chessGameService.move(gameId, moveDto));
    }
}
