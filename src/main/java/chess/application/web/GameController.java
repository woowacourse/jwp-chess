package chess.application.web;

import chess.dto.BoardResponse;
import chess.dto.GameRequest;
import chess.dto.MoveRequest;
import chess.dto.StatusResponse;
import java.net.URI;
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
    public ResponseEntity<Void> save(@RequestBody GameRequest gameRequest) {
        Long gameId = gameService.save(gameRequest);
        return ResponseEntity.created(URI.create("/game/" + gameId)).build();
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<StatusResponse> findStatus(@PathVariable Long id) {
        StatusResponse statusResponse = gameService.findStatus(id);
        return ResponseEntity.ok(statusResponse);
    }

    @PutMapping("/{id}/move")
    public ResponseEntity<BoardResponse> update(@PathVariable Long id, @RequestBody MoveRequest moveRequest) {
        BoardResponse boardResponse = gameService.update(id, moveRequest);
        return ResponseEntity.ok(boardResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id, @RequestBody String password) {
        gameService.delete(id, password);
        return ResponseEntity.noContent().build();
    }
}
