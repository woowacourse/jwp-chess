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

    @GetMapping(value = "/games")
    public GameRoomListDto loadGameRooms() {
        return springChessService.loadGameRooms();
    }

    @PostMapping(value = "/games")
    public GameRoomDto createGameRoom(@RequestBody GameRoomNameDto gameRoomNameDto) {
        final String roomName = gameRoomNameDto.getName();
        return springChessService.createGameRoom(roomName);
    }

    @PostMapping(value = "/games/{id}")
    public ChessGameDto startNewGame(@PathVariable int id) {
        return springChessService.startNewGame(id);
    }

    @GetMapping(value = "/games/{id}")
    public ChessGameDto loadSavedGame(@PathVariable int id) {
        return springChessService.loadSavedGame(id);
    }

    @DeleteMapping(value = "/games/{id}")
    public ResponseEntity<String> deleteGame(@PathVariable int id) {
        springChessService.deleteGame(id);
        return ResponseEntity.ok("success");
    }

    @PostMapping(value = "/games/{id}/move")
    public ChessGameDto move(@RequestBody MoveRequestDto moveRequestDto, @PathVariable int id) {
        final String start = moveRequestDto.getStart();
        final String destination = moveRequestDto.getDestination();
        return springChessService.move(id, start, destination);
    }
}
