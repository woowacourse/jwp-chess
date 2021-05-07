package chess.controller;

import chess.controller.dto.GameDto;
import chess.controller.dto.GameStatusDto;
import chess.controller.dto.MoveDto;
import chess.service.ChessService;
import chess.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ChessApiController {
    private final ChessService chessService;
    private final GameService gameService;

    public ChessApiController(ChessService chessService, GameService gameService) {
        this.chessService = chessService;
        this.gameService = gameService;
    }

    @GetMapping("/games")
    public ResponseEntity<List<GameDto>> allGame() {
        List<GameDto> games = gameService.allGame();
        return ResponseEntity.ok().body(games);
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
        chessService.move(gameId, moveDto);
        return ResponseEntity.ok()
                             .body(chessService.load(gameId));
    }
}
