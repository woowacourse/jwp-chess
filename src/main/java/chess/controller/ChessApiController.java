package chess.controller;

import chess.controller.dto.GameDto;
import chess.controller.dto.GameStatusDto;
import chess.controller.dto.MoveDto;
import chess.domain.game.Game;
import chess.service.ChessService;
import chess.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ChessApiController {
    private final ChessService chessService;
    private final GameService gameService;

    public ChessApiController(ChessService chessService, GameService gameService) {
        this.chessService = chessService;
        this.gameService = gameService;
    }

    @PostMapping("/games")
    public ResponseEntity<Long> createRoom(@RequestBody GameDto gameDto) {
        Long createdGameId = gameService.create(gameDto);
        return ResponseEntity.ok().body(createdGameId);
    }

    @GetMapping("/games/{gameId}/load")
    public ResponseEntity<GameStatusDto> loadGame(@PathVariable Long gameId) {
        return ResponseEntity.ok()
                             .body(chessService.load(gameId));
    }

    @DeleteMapping("/games/{gameId}")
    public ResponseEntity deleteRoom(@PathVariable("gameId") Long gameId) {
        gameService.delete(gameId);
        return ResponseEntity.ok()
                             .build();
    }

    @PostMapping("/games/{gameId}/move")
    public ResponseEntity<GameStatusDto> move(@PathVariable("gameId") Long gameId, @RequestBody MoveDto moveDto) {
        return ResponseEntity.ok()
                             .body(chessService.load(gameId));
    }
}
