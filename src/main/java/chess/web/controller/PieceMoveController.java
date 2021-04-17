package chess.web.controller;


import chess.web.controller.dto.request.MoveRequestDTO;
import chess.web.controller.dto.response.MoveResponseDto;
import chess.web.service.ChessGameService;
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
    public MoveResponseDto movePiece(@RequestBody MoveRequestDTO moveRequestDTO) {
        return chessGameService.movePiece(moveRequestDTO);
    }
}
