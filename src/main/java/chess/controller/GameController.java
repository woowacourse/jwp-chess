package chess.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import chess.dto.PathResponse;
import chess.service.GameService;

@RestController
public class GameController {

    private final GameService service;

    public GameController(GameService service) {
        this.service = service;
    }

    @PatchMapping(path = "/game/{gameId}/start")
    public ResponseEntity<PathResponse> start(@PathVariable Long gameId) {
        service.startGame(gameId);
        return ResponseEntity.ok().body(new PathResponse("/main/" + gameId));
    }

    @PatchMapping(path = "/game/{gameId}/end")
    public ResponseEntity<PathResponse> end(@PathVariable Long gameId) {
        service.finishGame(gameId);
        return ResponseEntity.ok().body(new PathResponse("/"));
    }
}
