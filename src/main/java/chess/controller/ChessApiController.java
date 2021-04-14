package chess.controller;

import chess.domain.chessgame.ChessGame;
import chess.dto.BoardRequestDto;
import chess.dto.PiecesResponseDto;
import chess.dto.ScoreResponseDto;
import chess.service.ChessService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessApiController {

    private final ChessService chessService;
    private ChessGame chessGame = new ChessGame();

    public ChessApiController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping(value = "/pieces")
    public PiecesResponseDto getPieces() {
        return chessService.getPieces(chessGame);
    }

    @PutMapping(value = "/board")
    public PiecesResponseDto putBoard(@RequestBody BoardRequestDto boardRequestDto) {
        return chessService.putBoard(boardRequestDto, chessGame);
    }

    @GetMapping(value = "/score")
    public ScoreResponseDto getScore(@RequestParam("color") String colorName){
        return chessService.getScore(colorName, chessGame);
    }

}
