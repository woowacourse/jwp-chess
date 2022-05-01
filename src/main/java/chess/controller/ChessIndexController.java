package chess.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import chess.dto.PathResponse;
import chess.dto.RoomRequest;
import chess.service.GameService;

@RestController
public class ChessIndexController {

    private final GameService gameService;

    public ChessIndexController(GameService gameService) {
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
        return respondPath(String.format(ChessViewController.MAIN_PATH_FORMAT, id));
    }

    @GetMapping(path = "/enter/{roomId}")
    public ResponseEntity<PathResponse> enter(@PathVariable Long roomId) {
        return respondPath(String.format(ChessViewController.MAIN_PATH_FORMAT, roomId));
    }

    @DeleteMapping(path = "/room")
    public ResponseEntity<PathResponse> delete(@RequestBody RoomRequest roomRequest) {
        gameService.deleteGame(roomRequest);
        return respondPath(ChessViewController.ROOT_PATH);
    }

    private ResponseEntity<PathResponse> respondPath(String path) {
        return ResponseEntity.ok().body(new PathResponse(path));
    }
}
