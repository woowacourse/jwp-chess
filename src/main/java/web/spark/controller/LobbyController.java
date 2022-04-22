package web.spark.controller;

import chess.Score;
import chess.piece.Color;
import java.math.BigDecimal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web.spark.dao.ChessGameDao;
import web.spark.dto.GameStatus;

@Controller
public class LobbyController {

    private final ChessGameDao chessGameDao;

    public LobbyController(ChessGameDao chessGameDao) {
        this.chessGameDao = chessGameDao;
    }

    @GetMapping("/")
    public String lobby(Model model) {
        model.addAttribute("chess-games", chessGameDao.findAll());
        return "index";
    }

    @PostMapping("/create-chess-game")
    public String createChessGame(@RequestParam String name) {
        Score initialScore = new Score(new BigDecimal("38.0"));
        chessGameDao.saveChessGame(name, GameStatus.READY, Color.WHITE, initialScore, initialScore);
        return "redirect:/";
    }
}
