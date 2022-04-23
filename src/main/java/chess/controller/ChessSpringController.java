package chess.controller;

import chess.dto.*;
import chess.service.ChessService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok().body(chessService.move(moveDto, chessService.getGame().getId()));
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

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handle(RuntimeException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
