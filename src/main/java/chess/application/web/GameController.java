package chess.application.web;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/game/status")
    public ResponseEntity<Map<String, Object>> status() {
        Map<String, Object> statusData = gameService.modelStatus();
        return ResponseEntity.ok().body(statusData);
    }

    @GetMapping("game/save/{id}")
    public ResponseEntity<Void> save(@PathVariable int id) {
        gameService.updateGame(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
