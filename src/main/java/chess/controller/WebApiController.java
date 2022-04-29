package chess.controller;

import chess.dto.ScoreDto;
import chess.service.ChessGameService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebApiController {

    private final ChessGameService chessGameService;

    public WebApiController(final ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping(path="/{roomNumber}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScoreDto> status(@PathVariable int roomNumber) {
        final ScoreDto score = chessGameService.getScore(roomNumber);
        return ResponseEntity.ok(score);
    }
}
