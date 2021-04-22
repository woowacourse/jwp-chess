package chess.web.controller;


import static chess.web.controller.ChessGameController.ENCRYPTED_PASSWORD;

import chess.web.controller.dto.request.MoveRequestDto;
import chess.web.controller.dto.response.MoveResponseDto;
import chess.web.service.ChessGameService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ChessGamePlayController {

    private final ChessGameService chessGameService;

    public ChessGamePlayController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @PostMapping("/move")
    public MoveResponseDto movePiece(
        @RequestBody MoveRequestDto moveRequestDto,
        @CookieValue(ENCRYPTED_PASSWORD) String encryptedPassword) {

        chessGameService.movePiece(moveRequestDto, encryptedPassword);
        return new MoveResponseDto(false);
    }

    @DeleteMapping("/games/{gameId}")
    public HttpEntity<HttpStatus> deleteGame(@PathVariable Long gameId) {
        chessGameService.endGame(gameId);
        return new HttpEntity<>(HttpStatus.OK);
    }
}
