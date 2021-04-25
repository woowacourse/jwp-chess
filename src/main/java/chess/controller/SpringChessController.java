package chess.controller;

import chess.service.SpringChessService;
import chess.webdto.view.ChessGameDto;
import chess.webdto.view.MoveRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("/game")
public class SpringChessController {
    private final SpringChessService springChessService;

    public SpringChessController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    @PostMapping
    public ChessGameDto startNewGame() {
        return springChessService.startNewGame();
    }

    @GetMapping(value = "/previous")
    public ChessGameDto loadPrevGame() {
        return springChessService.loadPreviousGame();
    }

    @PostMapping(path = "/move")
    public ChessGameDto move(@RequestBody MoveRequestDto moveRequestDto) {
        return springChessService.move(moveRequestDto);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> handleIllegalArgumentException() {
        return ResponseEntity.badRequest().body("unavailable");
    }

    @ExceptionHandler({SQLException.class})
    public ResponseEntity<String> handleSQLException() {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("fail");
    }

}
