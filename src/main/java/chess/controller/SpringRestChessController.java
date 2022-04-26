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
import chess.dto.RoomRequest;
import chess.service.GameService;

@RestController
public class SpringRestChessController {

    private static final String MAIN_PATH_FORMAT = "/main?roomId=%d";

    private final GameService gameService;

    @Autowired
    public SpringRestChessController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<PathResponse> create(@RequestBody RoomRequest roomRequest) {
        final Long id = gameService.createNewGame(roomRequest);
        return respondPath(String.format(MAIN_PATH_FORMAT, id));
    }

    @GetMapping(path = "/enter")
    public ResponseEntity<PathResponse> enter(@RequestParam("roomId") Long roomId) {
        return respondPath(String.format(MAIN_PATH_FORMAT, roomId));
    }

    @GetMapping(path = "/start")
    public ResponseEntity<PathResponse> start(@RequestParam("roomId") Long roomId) {
        gameService.startGame(roomId);
        return respondPath(String.format(MAIN_PATH_FORMAT, roomId));
    }

    @GetMapping(path = "/end")
    public ResponseEntity<PathResponse> end(@RequestParam("roomId") Long roomId) {
        gameService.finishGame(roomId);
        return respondPath("/");
    }

    @PostMapping(path = "/move")
    public ResponseEntity<GameStateResponse> move(
        @RequestParam("roomId") Long roomId,
        @RequestBody String body) {
        final Arguments arguments = Arguments.ofJson(body, Command.MOVE.getParameters());
        GameState state = gameService.moveBoard(roomId, arguments);
        return ResponseEntity.ok().body(GameStateResponse.of(state));
    }

    @GetMapping(path = "/status")
    public ResponseEntity<Map<String, Object>> status(@RequestParam("roomId") Long roomId) {
        GameState state = gameService.readGameState(roomId);
        return ResponseEntity.ok().body(Map.of("board", BoardResponse.of(state.getPointPieces()),
            "score", state.getColorScore())
        );
    }

    private ResponseEntity<PathResponse> respondPath(String path) {
        return ResponseEntity.ok().body(new PathResponse(path));
    }
}
