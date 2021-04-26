package chess.controller;

import chess.service.game.ChessGameService;
import dto.ChessGameDto;
import dto.MoveDto;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class ChessGameController {
    private final ChessGameService chessGameService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> checkChessGameException(Exception exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler({MissingRequestCookieException.class})
    public ResponseEntity<String> checkCookieMissing(Exception exception) {
        return ResponseEntity.badRequest().body("로그인을 해 주세요.");
    }

    public ChessGameController(final ChessGameService chessGameService, final SimpMessagingTemplate simpMessagingTemplate) {
        this.chessGameService = chessGameService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChessGameDto> load(@CookieValue(value = "user") String cookie, @PathVariable Long id) {
        ChessGameDto chessGameDto = chessGameService.load(id);
        simpMessagingTemplate.convertAndSend("/topic/game/" + id, chessGameDto);
        return ResponseEntity.ok().body(chessGameDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChessGameDto> movePiece(@CookieValue(value = "user") String cookie, @PathVariable("id") Long gameId, @RequestBody MoveDto moveDto) {
        return ResponseEntity.ok().body(chessGameService.move(gameId, moveDto));
    }
}
