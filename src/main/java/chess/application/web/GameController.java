package chess.application.web;

import chess.dto.MoveRequest;
import chess.dto.GameRequest;
import chess.dto.StatusResponse;
import java.net.URI;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/games")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody GameRequest gameRequest) {
        Long gameId = gameService.create(gameRequest);
        return ResponseEntity.created(URI.create("/game/" + gameId)).build();
    }

    @GetMapping("/status")
    public ResponseEntity<StatusResponse> status() {
        StatusResponse statusResponse = gameService.modelStatus();
        return ResponseEntity.ok(statusResponse);
    }

    @PutMapping("/{id}/move")
    public ResponseEntity<Map<String, Object>> update(@RequestBody MoveRequest moveRequest, @PathVariable Long id) {
        gameService.move(moveRequest);
        gameService.updateGame(id);
        return ResponseEntity.ok(gameService.modelPlayingBoard());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id, @RequestBody String password) {
        gameService.deleteGame(id, password);
        return ResponseEntity.noContent().build();
    }
}
