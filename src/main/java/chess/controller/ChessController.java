package chess.controller;

import chess.dto.*;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChessController {
    private static final int ROOM_ID = 1;
    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/board")
    public ResponseEntity<BoardDto> getBoard() {
        return ResponseEntity.ok().body(chessService.getInitialBoard(ROOM_ID));
    }

    @PostMapping(value = "/move")
    public ResponseEntity<GameStateDto> move(@RequestBody MoveDto moveDto) {
        return ResponseEntity.ok().body(chessService.move(moveDto, ROOM_ID));
    }

    @GetMapping("/status")
    public ResponseEntity<ScoreDto> getStatus() {
        return ResponseEntity.ok().body(chessService.getStatus(ROOM_ID));
    }

    @PostMapping("/reset")
    public ResponseEntity<BoardDto> reset() {
        return ResponseEntity.ok().body(chessService.resetBoard(ROOM_ID));
    }

    @PostMapping("/end")
    public ResponseEntity<GameStateDto> end() {
        return ResponseEntity.ok().body(chessService.end(ROOM_ID));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handle(RuntimeException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
