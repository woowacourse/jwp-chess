package chess.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import chess.domain.game.GameState;
import chess.dto.Arguments;
import chess.dto.BoardResponse;
import chess.dto.GameStateResponse;
import chess.dto.PathResponse;
import chess.service.GameService;

@RestController
public class SpringRestChessController {

    private final GameService gameService;

    @Autowired
    public SpringRestChessController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping(path = "/create")
    public ResponseEntity<PathResponse> create(@RequestParam("room_name") String roomName) {
        gameService.createNewGame(roomName);
        return respondPath("/main?room_name=" + roomName);
    }

    @GetMapping(path = "/enter")
    public ResponseEntity<PathResponse> enter(@RequestParam("room_name") String roomName) {
        return respondPath("/main?room_name=" + roomName);
    }

    @GetMapping(path = "/start")
    public ResponseEntity<PathResponse> start(@RequestParam("room_name") String roomName) {
        gameService.startGame(roomName);
        return respondPath("/main?room_name=" + roomName);
    }

    @GetMapping(path = "/end")
    public ResponseEntity<PathResponse> end(@RequestParam("room_name") String roomName) {
        gameService.finishGame(roomName);
        return respondPath("/");
    }

    @PostMapping(path = "/move")
    public ResponseEntity<GameStateResponse> move(@RequestParam("room_name") String roomName,
        @RequestBody String body) {
        final Arguments arguments = Arguments.ofJson(body, Command.MOVE.getParameters());
        GameState state = gameService.moveBoard(roomName, arguments);
        return ResponseEntity.ok().body(GameStateResponse.of(state));
    }

    @GetMapping(path = "/status")
    public ResponseEntity<Map<String, Object>> status(@RequestParam("room_name") String roomName) {
        GameState state = gameService.readGameState(roomName);
        return ResponseEntity.ok().body(Map.of("board", BoardResponse.of(state.getPointPieces()),
            "score", state.getColorScore())
        );
    }

    private ResponseEntity<PathResponse> respondPath(String path) {
        return ResponseEntity.ok().body(new PathResponse(path));
    }
}
