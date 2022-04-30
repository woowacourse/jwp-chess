package chess.controller;

import chess.dto.MoveDto;
import chess.service.ChessGameService;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class PlayController {
    private static final String LOCALHOST_8080 = "http://localhost:8080";

    private final ChessGameService chessGameService;

    public PlayController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @PostMapping("/end")
    public ResponseEntity<Void> endGame(@RequestParam String id) {
        chessGameService.changeToEnd(id);
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .location(URI.create(LOCALHOST_8080)).build();
    }

    @PutMapping("/{id}/move")
    public ResponseEntity<String> move(@PathVariable String id, @RequestBody MoveDto moveDto) {
        chessGameService.move(id, moveDto);

        if (!chessGameService.calculateGameResult(id).getWinner().equals("없음")) {
            chessGameService.changeToEnd(id);
        }
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exception(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
