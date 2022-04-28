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

    @GetMapping("/loadBoard/{id}")
    public ResponseEntity<BoardDto> loadBoard(@PathVariable Long id) {
        BoardDto initialBoard = chessService.getBoard(id);
        return ResponseEntity.ok().body(initialBoard);
    }

    @GetMapping("/loadGames")
    public ResponseEntity<GamesTempDto> loadGames() {
        return ResponseEntity.ok().body(new GamesTempDto(chessService.getGameList()));
    }

    @PostMapping(value = "/move/{id}")
    public ResponseEntity<GameStateDto> move(@PathVariable Long id, @RequestBody MoveDto moveDto) {
        return ResponseEntity.ok().body(chessService.move(id, moveDto));
    }

    @GetMapping("/status")
    public ResponseEntity<ScoreDto> getStatus() {
        return ResponseEntity.ok().body(chessService.getStatus());
    }

    @PostMapping("/reset/{id}")
    public ResponseEntity<BoardDto> resetGame(@PathVariable Long id) {
        chessService.resetBoard(chessService.findRoom(id), id);
        return ResponseEntity.ok().body(chessService.getBoard(id));
    }

    @PostMapping("/end/{id}")
    public ResponseEntity<GameStateDto> endGame(@PathVariable Long id) {
        chessService.updateEndStatus(id);
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
