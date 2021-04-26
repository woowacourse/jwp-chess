package chess.controller.web;

import chess.dto.ChessGameDto;
import chess.dto.MoveRequestDto;
import chess.service.SpringChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
public class ChessRestController {
    private final SpringChessService springChessService;

    public ChessRestController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    @GetMapping("/{roomNo}/start")
    public ResponseEntity<ChessGameDto> startGame(@PathVariable("roomNo") int roomNo) {
        ChessGameDto chessGameDto = springChessService.loadRoom(roomNo);
        return ResponseEntity.ok(chessGameDto);
    }

    @PutMapping(value = "/{roomNo}/restart")
    public ResponseEntity<ChessGameDto> restartGame(@PathVariable("roomNo") int roomNo) {
        ChessGameDto chessGameDto = springChessService.resetRoom(roomNo);
        return ResponseEntity.ok(chessGameDto);
    }

    @PutMapping(value = "/{roomNo}/move")
    public ResponseEntity<ChessGameDto> movePiece(@PathVariable("roomNo") int roomNo, @RequestBody MoveRequestDto moveRequestDto) {
        ChessGameDto chessGameDto = springChessService.movePiece(roomNo, moveRequestDto);
        return ResponseEntity.ok(chessGameDto);
    }

    @DeleteMapping("/{roomNo}")
    public ResponseEntity<Void> deleteRoom(@PathVariable("roomNo") int roomNo) {
        springChessService.deleteRoom(roomNo);
        return ResponseEntity.ok().build();
    }
}