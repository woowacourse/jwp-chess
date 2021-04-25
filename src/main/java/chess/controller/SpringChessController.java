package chess.controller;

import chess.service.SpringChessService;
import chess.webdto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SpringChessController {
    private final SpringChessService springChessService;

    public SpringChessController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    @PostMapping(value = "/games")
    public GameRoomDto createGameRoom(@RequestBody GameRoomNameDto gameRoomNameDto) {
        final String roomName = gameRoomNameDto.getName();
        return springChessService.createGameRoom(roomName);
    }

    @GetMapping(value = "/games")
    public GameRoomListDto loadGameRooms() {
        return springChessService.loadGameRooms();
    }

    @PostMapping(value = "/games/{roomId}")
    public ChessGameDto startNewGame(@PathVariable int roomId) {
        return springChessService.createChessGame(roomId);
    }

    @GetMapping(value = "/games/{roomId}")
    public ChessGameDto loadSavedGame(@PathVariable int roomId) {
        return springChessService.readChessGame(roomId);
    }

    @DeleteMapping(value = "/games/{roomId}")
    public ResponseEntity<String> deleteGame(@PathVariable int roomId) {
        springChessService.deleteChessGame(roomId);
        return ResponseEntity.ok("success");
    }

    @PostMapping(value = "/games/{roomId}/move")
    public ChessGameDto move(@RequestBody MoveRequestDto moveRequestDto, @PathVariable int roomId) {
        final String start = moveRequestDto.getStart();
        final String destination = moveRequestDto.getDestination();
        return springChessService.move(roomId, start, destination);
    }
}
