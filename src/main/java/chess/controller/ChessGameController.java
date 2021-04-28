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
    public ResponseEntity<ChessGameDto> movePiece(@CookieValue(value = "user") String cookie, @PathVariable("id") Long id,
                                                  @RequestBody MoveDto moveDto) {
        ChessGameDto chessGameDto = chessGameService.move(id, moveDto);
        simpMessagingTemplate.convertAndSend("/topic/game/" + id, chessGameDto);
        return ResponseEntity.ok().body(chessGameDto);
    }
}
