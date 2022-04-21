package chess;

import chess.domain.ChessGame;
import chess.domain.GameResult;
import chess.domain.piece.Color;
import chess.service.DBService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ResultController {
    private final chess.service.DBService DBService;

    public ResultController() {
        this.DBService = new DBService();
    }

    @GetMapping("/status")
    public String showResult(@RequestParam String gameID, Model model) {

        GameResult gameResult = DBService.getGameResult(gameID);
        model.addAttribute("whiteScore", gameResult.calculateScore(Color.WHITE));
        model.addAttribute("blackScore", gameResult.calculateScore(Color.BLACK));
        return "status";
    }
}
