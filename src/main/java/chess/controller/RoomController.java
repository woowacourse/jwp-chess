package chess.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import chess.dto.PathResponse;
import chess.dto.RoomRequest;
import chess.dto.RoomResponse;
import chess.service.GameService;

@RestController
public class RoomController {

    private final GameService gameService;

    public RoomController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping(path = "/rooms")
    public ResponseEntity<List<RoomResponse>> rooms() {
        List<RoomResponse> responses = gameService.readGameRooms()
            .stream()
            .map(RoomResponse::from)
            .collect(Collectors.toList());
        return ResponseEntity.ok().body(responses);
    }

    @PostMapping(path = "/room")
    public ResponseEntity<PathResponse> create(@RequestBody RoomRequest roomRequest) {
        final Long roomId = gameService.createNewGame(roomRequest);
        final Long gameId = gameService.findGameIdByRoomId(roomId);
        return respondPath(String.format(ChessViewController.MAIN_PATH_FORMAT, gameId));
    }

    @GetMapping(path = "/room/{roomId}")
    public ResponseEntity<PathResponse> enter(@PathVariable Long roomId) {
        Long gameId = gameService.findGameIdByRoomId(roomId);
        return respondPath(String.format(ChessViewController.MAIN_PATH_FORMAT, gameId));
    }

    @DeleteMapping(path = "/room/{roomId}")
    public ResponseEntity<PathResponse> delete(@PathVariable Long roomId, @RequestBody RoomRequest roomRequest) {
        gameService.removeRoom(roomId, roomRequest);
        return respondPath(ChessViewController.ROOT_PATH);
    }

    private ResponseEntity<PathResponse> respondPath(String path) {
        return ResponseEntity.ok().body(new PathResponse(path));
    }
}
