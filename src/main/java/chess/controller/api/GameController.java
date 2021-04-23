package chess.controller.api;

import chess.dto.ChessGameDto;
import chess.dto.MoveDto;
import chess.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/room")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/{id}/game-info")
    public ResponseEntity<ChessGameDto> loadGame(@PathVariable String id) {
        return ResponseEntity.ok(new ChessGameDto(gameService.loadGame(id)));
    }

    @PutMapping(path = "/{id}/move")
    public ResponseEntity<ChessGameDto> move(@PathVariable String id, @RequestBody MoveDto moveDTO) {
        gameService.move(id, moveDTO);
        return loadGame(id);
    }

    @PostMapping("/{id}/finish")
    public ResponseEntity<Void> finish(@PathVariable String id) {
        gameService.finish(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/restart")
    public ResponseEntity<ChessGameDto> restart(@PathVariable String id) {
        return ResponseEntity.ok(new ChessGameDto(gameService.restart(id)));
    }
}
