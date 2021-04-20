package chess.controller;

import chess.service.SpringChessService;
import chess.webdto.ChessGameDto;
import chess.webdto.MoveRequestDto;
import org.springframework.http.MediaType;
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

    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ChessGameDto startNewGame() {
        return springChessService.startNewGame();
    }

    @GetMapping(value = "/previous", produces = MediaType.APPLICATION_JSON_VALUE)
    public ChessGameDto loadPrevGame() {
        return springChessService.loadPreviousGame();
    }

    @PutMapping(path = "/move")
    public ChessGameDto move(@RequestBody MoveRequestDto moveRequestDTO) {
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
