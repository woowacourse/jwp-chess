package chess.controller;

import chess.service.SpringChessService;
import chess.webdto.ChessGameDTO;
import chess.webdto.MoveRequestDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
public class SpringChessController {
    private SpringChessService springChessService;

    public SpringChessController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    @GetMapping(value = "/startNewGame", produces = MediaType.APPLICATION_JSON_VALUE)
    public ChessGameDTO startNewGame() {
        return springChessService.startNewGame();
    }

    @GetMapping(value = "/loadPrevGame", produces = MediaType.APPLICATION_JSON_VALUE)
    public ChessGameDTO loadPrevGame() {
        return springChessService.loadPreviousGame();
    }

    @PostMapping(value = "/move", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ChessGameDTO move(@RequestBody MoveRequestDto moveRequestDTO) {
        final String start = moveRequestDTO.getStart();
        final String destination = moveRequestDTO.getDestination();
        return springChessService.move(start, destination);
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
