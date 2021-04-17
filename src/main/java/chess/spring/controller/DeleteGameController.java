package chess.spring.controller;

import chess.spring.controller.dto.request.GameDeleteDto;
import chess.spring.service.ChessGameService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleteGameController {

    private final ChessGameService chessGameService;

    public DeleteGameController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @PostMapping("/delete")
    public HttpEntity<HttpStatus> deleteGame(@RequestBody GameDeleteDto gameDeleteDto) {
        chessGameService.deleteGame(gameDeleteDto.getGameId());
        return new HttpEntity<>(HttpStatus.OK);
    }
}
