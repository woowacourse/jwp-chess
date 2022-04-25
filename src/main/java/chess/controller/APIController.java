package chess.controller;

import chess.dto.ScoreDto;
import chess.service.ChessGameService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class APIController {

    private final ChessGameService chessGameService;

    public APIController(final ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping(path="/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScoreDto> status() {
        final ScoreDto score = chessGameService.getScore();
        return ResponseEntity.ok(score);
    }
}
