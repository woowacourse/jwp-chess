package chess.controller;

import chess.dto.*;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChessSpringController {

    @GetMapping("/board")
    public ResponseEntity<BoardDto> getBoard() {
        ChessService chessService = new ChessService(1);
        BoardDto initialBoard = chessService.getInitialBoard();
        return ResponseEntity.ok().body(initialBoard);
    }

    @PostMapping(value = "/move")
    public ResponseEntity<GameStateDto> move(@RequestBody MoveDto moveDto) {
        ChessService chessService = new ChessService(1);
        return ResponseEntity.ok().body(chessService.move(moveDto, 1));
    }

    @GetMapping("/status")
    public ResponseEntity<ScoreDto> getStatus() {
        ChessService chessService = new ChessService(1);
        return ResponseEntity.ok().body(chessService.getStatus());
    }

    @PostMapping("/reset")
    public ResponseEntity<BoardDto> reset() {
        ChessService chessService = new ChessService(1);
        return ResponseEntity.ok().body(chessService.resetBoard(1));
    }

    @PostMapping("/end")
    public ResponseEntity<GameStateDto> end() {
        ChessService chessService = new ChessService(1);
        return ResponseEntity.ok().body(chessService.end(1));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handle(RuntimeException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
