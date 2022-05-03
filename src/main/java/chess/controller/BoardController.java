package chess.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import chess.domain.game.GameState;
import chess.dto.GameStateResponse;
import chess.dto.RouteRequest;
import chess.dto.ScoreResponse;
import chess.service.GameService;

@RestController
public class BoardController {

    private final GameService service;

    public BoardController(GameService service) {
        this.service = service;
    }

    @PatchMapping(path = "/board/{gameId}/move")
    public ResponseEntity<GameStateResponse> move(
        @PathVariable Long gameId,
        @RequestBody RouteRequest routeRequest) {
        GameState state = service.moveBoard(gameId, routeRequest);
        return ResponseEntity.ok().body(GameStateResponse.of(state));
    }

    @GetMapping(path = "/board/{gameId}/status")
    public ResponseEntity<ScoreResponse> status(@PathVariable Long gameId) {
        System.out.println("WHAT THE FUCK");
        GameState state = service.readGameState(gameId);
        return ResponseEntity.ok().body(ScoreResponse.of(state.getColorScore()));
    }

}
