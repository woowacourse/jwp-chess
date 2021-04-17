package chess.spring.controller;


import chess.spring.controller.dto.request.MoveRequestDTO;
import chess.spring.controller.dto.response.MoveResponseDto;
import chess.spring.service.ChessGameService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class PieceMoveController {

    private final ChessGameService chessGameService;

    public PieceMoveController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @PostMapping("/move")
    @ResponseBody
    public MoveResponseDto movePiece(@RequestBody MoveRequestDTO moveRequestDTO) {
        return chessGameService.movePiece(moveRequestDTO);
    }
}
