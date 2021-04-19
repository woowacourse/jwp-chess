package chess.web.controller;


import static chess.web.controller.ChessGameController.ENCRYPTED_PASSWORD;

import chess.web.controller.dto.request.MoveRequestDto;
import chess.web.controller.dto.response.MoveResponseDto;
import chess.web.service.ChessGameService;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PieceMoveController {

    private final ChessGameService chessGameService;

    public PieceMoveController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @PostMapping("/move")
    public MoveResponseDto movePiece(
        @RequestBody MoveRequestDto moveRequestDto,
        @CookieValue(ENCRYPTED_PASSWORD) String encryptedPassword) {

        moveRequestDto.setEncryptedPassword(encryptedPassword);
        chessGameService.movePiece(moveRequestDto);
        return new MoveResponseDto(false);
    }
}
