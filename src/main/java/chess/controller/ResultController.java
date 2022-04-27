package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import chess.domain.GameResult;
import chess.domain.piece.Color;
import chess.service.ChessService;

@Controller
public class ResultController {
    private final ChessService chessService;

    public ResultController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/results")
    public String showResult(@RequestParam String gameID, Model model) {

        GameResult gameResult = chessService.getGameResult(gameID);
        model.addAttribute("whiteScore", gameResult.calculateScore(Color.WHITE));
        model.addAttribute("blackScore", gameResult.calculateScore(Color.BLACK));
        return "status";
    }
}
