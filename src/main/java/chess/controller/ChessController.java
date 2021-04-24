package chess.controller;

import chess.domain.player.Round;
import chess.dto.ScoreDto;
import chess.service.ChessService;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChessController {
    public static final Gson GSON = new Gson();

    private final ChessService chessService;

    public ChessController(final ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/rooms/{roomId}")
    public String chess(final Model model, @PathVariable int roomId) {
        Round loadedRound = chessService.getStoredRound(roomId);
        String board = GSON.toJson(loadedRound.getBoard());
        model.addAttribute("jsonFormatChessBoard", board);

        String currentTurn = loadedRound.getCurrentTurn();
        model.addAttribute("currentTurn", currentTurn);

        double whiteScore = loadedRound.getPlayerScore("white");
        double blackScore = loadedRound.getPlayerScore("black");

        ScoreDto scoreDto = new ScoreDto(whiteScore, blackScore);
        model.addAttribute("score", scoreDto);

        return "chess";
    }
}