package chess.controller;

import chess.domain.Room;
import chess.service.ChessService;
import dto.ChessGameDto;
import dto.MoveDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SpringChessController {
    @Autowired
    ChessService chessService;

    @GetMapping("/room")
    public ResponseEntity<List<Room>> loadAllRoom() {
        return chessService.loadAllRoom();
    }

    @PostMapping("/room/{id}")
    public ResponseEntity<ChessGameDto> loadGame(@PathVariable("id") Long roodId, @RequestBody Room room) {
        return chessService.loadGame(roodId, room);
    }

    @PostMapping("/room")
    public void createRoom(@RequestBody Room room) {
        chessService.createRoom(room);
    }

    @PutMapping("/room/{id}")
    public ResponseEntity<ChessGameDto> movePiece(@PathVariable("id") Long roodId, @RequestBody MoveDto moveDto) {
        return chessService.movePiece(roodId, moveDto);
    }
}
