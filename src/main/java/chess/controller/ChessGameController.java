package chess.controller;

import chess.service.game.ChessGameService;
import dto.ChessGameDto;
import dto.MoveDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class ChessGameController {
    private final ChessGameService chessGameService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public ChessGameController(final ChessGameService chessGameService, final SimpMessagingTemplate simpMessagingTemplate) {
        this.chessGameService = chessGameService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @PutMapping("/{id}/move")
    @ResponseBody
    public ResponseEntity movePiece(@CookieValue(value = "user") String cookie, @PathVariable("id") Long id,
                                                  @RequestBody MoveDto moveDto) {
        ChessGameDto chessGameDto = chessGameService.move(id, moveDto);
        simpMessagingTemplate.convertAndSend("/topic/game/" + id, chessGameDto);
        return ResponseEntity.ok().build();
    }
}
