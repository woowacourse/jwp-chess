package chess.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    private static final String MAIN_PATH_FORMAT = "/main/%d";
    private static final String ROOT_PATH = "/";

    private final GameService gameService;

    public SpringRestChessController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping(path = "/rooms")
    public ResponseEntity<Map<Long, String>> rooms() {
        final Map<Long, String> gameRooms = gameService.readGameRooms();
        return ResponseEntity.ok().body(gameRooms);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<PathResponse> create(@RequestBody RoomRequest roomRequest) {
        final Long id = gameService.createNewGame(roomRequest);
        return respondPath(String.format(MAIN_PATH_FORMAT, id));
    }

    @GetMapping(path = "/enter/{roomId}")
    public ResponseEntity<PathResponse> enter(@PathVariable Long roomId) {
        return respondPath(String.format(MAIN_PATH_FORMAT, roomId));
    }

    @GetMapping(path = "/start/{roomId}")
    public ResponseEntity<PathResponse> start(@PathVariable Long roomId) {
        gameService.startGame(roomId);
        return respondPath(String.format(MAIN_PATH_FORMAT, roomId));
    }

    @GetMapping(path = "/end/{roomId}")
    public ResponseEntity<PathResponse> end(@PathVariable Long roomId) {
        gameService.finishGame(roomId);
        return respondPath(ROOT_PATH);
    }

    @PostMapping(path = "/move/{roomId}")
    public ResponseEntity<GameStateResponse> move(
        @PathVariable Long roomId,
        @RequestBody String body) {
        final Arguments arguments = Arguments.ofJson(body, Command.MOVE.getParameters());
        GameState state = gameService.moveBoard(roomId, arguments);
        return ResponseEntity.ok().body(GameStateResponse.of(state));
    }

    @GetMapping(path = "/status/{roomId}")
    public ResponseEntity<Map<String, Object>> status(@PathVariable Long roomId) {
        GameState state = gameService.readGameState(roomId);
        return ResponseEntity.ok().body(Map.of("board", BoardResponse.of(state.getPointPieces()),
            "score", state.getColorScore())
        );
    }

    private ResponseEntity<PathResponse> respondPath(String path) {
        return ResponseEntity.ok().body(new PathResponse(path));
    }
}
