package chess.web.controller;

import chess.service.ChessService;
import chess.service.dto.BoardDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiChessController {
    private final ChessService chessService;

    public ApiChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PutMapping("/new-board/{gameId}")
    public ResponseEntity<Object> initBoard(@PathVariable int gameId) {
        chessService.initGame(gameId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/board/{gameId}")
    public ResponseEntity<BoardDto> getBoardPieces(@PathVariable int gameId) {
        return ResponseEntity.ok(chessService.getBoard(gameId));
    }

    @PostMapping("/move/{gameId}")
    public ResponseEntity<Object> requestMove(@PathVariable int gameId, @RequestParam String from,
                              @RequestParam String to) {
        chessService.move(gameId, from, to);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/game-end/{gameId}")
    public ResponseEntity<Object> requestEndGame(@PathVariable int gameId) {
        chessService.endGame(gameId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/game/{gameId}")
    public ResponseEntity<Integer> deleteGame(@PathVariable int gameId) {
        int affectedRows = chessService.deleteGame(gameId);
        return ResponseEntity.ok(affectedRows);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleError(Exception ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
