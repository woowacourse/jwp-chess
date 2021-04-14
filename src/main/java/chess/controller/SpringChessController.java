package chess.controller;

import chess.domain.ChessGame;
import chess.domain.DTO.BoardDTO;
import chess.domain.DTO.MoveInfoDTO;
import chess.domain.DTO.ScoreDTO;
import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;

@Controller
public class SpringChessController {
    private final ChessService chessService;
    private final ChessGame chessGame;

    public SpringChessController(ChessService chessService) {
        this.chessService = chessService;
        this.chessGame = new ChessGame();
    }

    @GetMapping("/")
    public String start() {
        return "chess.html";
    }

    @GetMapping("/loadSavedBoard")
    @ResponseBody
    public BoardDTO loadSavedBoard() throws SQLException {
        return chessService.getSavedBoardInfo(chessGame);
    }

    @GetMapping("/resetBoard")
    @ResponseBody
    public BoardDTO resetBoard() throws SQLException {
        return chessService.initiateBoard(chessGame);
    }

    @GetMapping("/scoreStatus")
    @ResponseBody
    public ScoreDTO scoreStatus() {
        return ScoreDTO.of(chessGame.scoreStatus());
    }

    @PostMapping("/move")
    @ResponseBody
    public BoardDTO move(@RequestBody MoveInfoDTO moveInfoDTO) throws SQLException {
        return chessService.move(chessGame, moveInfoDTO);
    }
}
