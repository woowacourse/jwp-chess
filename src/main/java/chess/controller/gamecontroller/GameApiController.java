package chess.controller.gamecontroller;

import chess.dto.ScoreDto;
import chess.service.ChessGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameApiController {

    private final ChessGameService chessGameService;

    public GameApiController(final ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping(path = "/{roomId}/status")
    public ResponseEntity<ScoreDto> status(@PathVariable int roomId) {
        final ScoreDto score = chessGameService.getScore(roomId);
        return ResponseEntity.ok(score);
    }
}
