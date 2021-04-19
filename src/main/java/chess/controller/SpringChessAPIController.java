package chess.controller;

import chess.domain.Room;
import chess.service.ChessService;
import dto.ChessGameDto;
import dto.MoveDto;
import dto.RoomDto;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SpringChessAPIController {
    private final ChessService chessService;

    public SpringChessAPIController(final ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<RoomDto>> loadAllRoom() {
        return chessService.loadAllRoom();
    }

    @PostMapping("/rooms/{id}")
    public ResponseEntity<ChessGameDto> loadGame(@PathVariable("id") Long roodId, @RequestBody Room room) {
        return chessService.loadGame(roodId, room);
    }

    @PostMapping("/rooms")
    public void createRoom(@RequestBody Room room) {
        chessService.createRoom(room);
    }

    @PutMapping("/room/{id}/pieces")
    public ResponseEntity<ChessGameDto> movePiece(@PathVariable("id") Long roodId, @RequestBody MoveDto moveDto) {
        return chessService.movePiece(roodId, moveDto);
    }
}
