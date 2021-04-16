package chess.controller;

import chess.domain.ChessGame;
import chess.domain.dto.BoardDto;
import chess.domain.dto.MoveInfoDto;
import chess.domain.dto.ScoreDto;
import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChessController {
    private final ChessService chessService;
    private final ChessGame chessGame;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
        this.chessGame = new ChessGame();
    }

    @GetMapping("/loadSavedBoard")
    public BoardDto loadSavedBoard() {
        return chessService.getSavedBoardInfo(chessGame);
    }

    @GetMapping("/resetBoard")
    public BoardDto resetBoard() {
        return chessService.initiateBoard(chessGame);
    }

    @GetMapping("/scoreStatus")
    public ScoreDto scoreStatus() {
        return ScoreDto.of(chessGame.scoreStatus());
    }

    @PostMapping("/move")
    public BoardDto move(@RequestBody MoveInfoDto moveInfoDTO) {
        return chessService.move(chessGame, moveInfoDTO);
    }
}
