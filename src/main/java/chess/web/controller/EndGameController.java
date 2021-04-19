package chess.web.controller;

import chess.web.service.ChessGameService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EndGameController {

    private final ChessGameService chessGameService;

    public EndGameController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @DeleteMapping("/games/{gameId}")
    public HttpEntity<HttpStatus> deleteGame(@PathVariable Long gameId) {
        chessGameService.endGame(gameId);
        return new HttpEntity<>(HttpStatus.OK);
    }
}
