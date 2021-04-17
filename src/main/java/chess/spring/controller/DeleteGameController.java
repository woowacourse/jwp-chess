package chess.spring.controller;

import chess.spring.controller.dto.request.GameDeleteRequestDto;
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
    public HttpEntity<HttpStatus> deleteGame(@RequestBody GameDeleteRequestDto gameDeleteRequestDto) {
        chessGameService.deleteGame(gameDeleteRequestDto.getGameId());
        return new HttpEntity<>(HttpStatus.OK);
    }
}
