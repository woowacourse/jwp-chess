package chess.controller;

import chess.domain.game.GameResult;
import chess.domain.piece.Color;
import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ResultController {
    private final ChessService chessService;

    public ResultController(final ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/status")
    public String showResult(@RequestParam String gameCode, Model model) {
        GameResult gameResult = chessService.getGameResult(gameCode);
        model.addAttribute("whiteScore", gameResult.calculateScore(Color.WHITE));
        model.addAttribute("blackScore", gameResult.calculateScore(Color.BLACK));
        return "final";
    }
}
