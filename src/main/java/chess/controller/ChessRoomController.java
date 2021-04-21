package chess.controller;

import chess.service.room.ChessRoomService;
import dto.ChessGameDto;
import dto.RoomDto;
import dto.RoomRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class ChessRoomController {
    private final ChessRoomService chessRoomService;

    @Autowired
    public ChessRoomController(final ChessRoomService chessRoomService) {
        this.chessRoomService = chessRoomService;
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<RoomDto>> loadAll() {
        return ResponseEntity.ok().body(chessRoomService.rooms());
    }

    @PostMapping("/room")
    public void create(@RequestBody RoomRequestDto roomRequestDto) {
        chessRoomService.create(roomRequestDto);
    }

    @PostMapping("/room/{id}")
    public ResponseEntity<ChessGameDto> enter(@PathVariable final String id, @RequestBody RoomRequestDto roomRequestDto) {
        return ResponseEntity.ok().body(chessRoomService.enter(roomRequestDto));
    }
}
