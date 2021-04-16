package chess.controller;

import chess.domain.ChessGame;
import chess.domain.dto.BoardDto;
import chess.domain.dto.MoveInfoDto;
import chess.domain.dto.ScoreDto;
import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChessController {
    private final ChessService chessService;
    private final ChessGame chessGame;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
        this.chessGame = new ChessGame();
    }

    @GetMapping("/")
    public String start() {
        return "chess.html";
    }

    @GetMapping("/loadSavedBoard")
    @ResponseBody
    public BoardDto loadSavedBoard() {
        return chessService.getSavedBoardInfo(chessGame);
    }

    @GetMapping("/resetBoard")
    @ResponseBody
    public BoardDto resetBoard() {
        return chessService.initiateBoard(chessGame);
    }

    @GetMapping("/scoreStatus")
    @ResponseBody
    public ScoreDto scoreStatus() {
        return ScoreDto.of(chessGame.scoreStatus());
    }

    @PostMapping("/move")
    @ResponseBody
    public BoardDto move(@RequestBody MoveInfoDto moveInfoDto) {
        return chessService.move(chessGame, moveInfoDto);
    }
}
