package chess.application.web;

import java.net.URI;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createGame(@RequestParam Map<String, String> request) {
        String title = request.get("title");
        String password = request.get("password");
        Long gameId = gameService.create(title, password);
        return ResponseEntity.created(URI.create("/game/" + gameId)).build();
    }

    @PostMapping("/game/{id}/move")
    public ResponseEntity<Map<String, Object>> move(@RequestParam Map<String, String> request, @PathVariable Long id) {
        gameService.move(request.get("source"), request.get("target"));
        gameService.updateGame(id);
        return ResponseEntity.ok().body(gameService.modelPlayingBoard());
    }

    @GetMapping("/game/status")
    public ResponseEntity<Map<String, Object>> status() {
        Map<String, Object> statusData = gameService.modelStatus();
        return ResponseEntity.ok().body(statusData);
    }

    @GetMapping("game/save/{id}")
    public ResponseEntity<Void> save(@PathVariable Long id) {
        gameService.updateGame(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id, @RequestParam Map<String, String> request) {
        gameService.deleteGame(id, request);
        return ResponseEntity.ok().build();
    }
}
