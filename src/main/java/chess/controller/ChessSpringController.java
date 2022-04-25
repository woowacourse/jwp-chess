package chess.controller;

import chess.dto.*;
import chess.service.ChessService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
public class ChessSpringController {

    private final ChessService chessService;

    public ChessSpringController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/board")
    public ResponseEntity<BoardDto> initBoard() {
        BoardDto initialBoard = chessService.getBoard();
        return ResponseEntity.ok().body(initialBoard);
    }

    @PostMapping(value = "/move")
    public ResponseEntity<GameStateDto> move(@RequestBody MoveDto moveDto) {
        return ResponseEntity.ok().body(chessService.move(moveDto));
    }

    @GetMapping("/status")
    public ResponseEntity<ScoreDto> getStatus() {
        return ResponseEntity.ok().body(chessService.getStatus());
    }

    @PostMapping("/reset")
    public ResponseEntity<BoardDto> reset() {
        chessService.resetBoard();
        return ResponseEntity.ok().body(chessService.getBoard());
    }

    @PostMapping("/end")
    public ResponseEntity<GameStateDto> end() {
        chessService.endGame();
        return ResponseEntity.ok().body(chessService.findWinner());
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<String> runTimeExceptionHandle(RuntimeException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> IllegalArgumentExceptionHandle(IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler({SQLException.class})
    public ResponseEntity<String> DBExceptionHandle(SQLException exception) {
        return ResponseEntity.internalServerError().body(exception.getMessage());
    }
}
