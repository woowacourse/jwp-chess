package chess.web.controller;


import static chess.web.controller.ChessGameController.ENCRYPTED_PASSWORD;

import chess.web.controller.dto.request.MoveRequestDto;
import chess.web.controller.dto.response.MoveCompleteResponseDto;
import chess.web.service.ChessGameService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GamePlayController {

    private static final String NOT_CORRECT_PASSWORD_ERROR_MESSAGE = "비밀번호가 일치하지 않습니다.";

    private final ChessGameService chessGameService;

    public GamePlayController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @PostMapping("/move")
    public ResponseEntity<MoveCompleteResponseDto> movePiece(
        @RequestBody MoveRequestDto moveRequestDto,
        @CookieValue(value = ENCRYPTED_PASSWORD, required = false) String encryptedPassword) {

        validateCookie(encryptedPassword);
        chessGameService.movePiece(moveRequestDto, encryptedPassword);
        return new ResponseEntity<>(new MoveCompleteResponseDto(), HttpStatus.OK);
    }

    private void validateCookie(String encryptedPassword) {
        if (encryptedPassword == null) {
            throw new IllegalArgumentException(NOT_CORRECT_PASSWORD_ERROR_MESSAGE);
        }
    }

    @DeleteMapping("/games/{gameId}")
    public HttpEntity<HttpStatus> deleteGame(@PathVariable Long gameId) {
        chessGameService.endGame(gameId);
        return new HttpEntity<>(HttpStatus.OK);
    }
}
