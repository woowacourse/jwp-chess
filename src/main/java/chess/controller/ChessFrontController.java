package chess.controller;

import chess.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChessFrontController {
    private final GameService gameService;

    public ChessFrontController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/")
    public String mainPage() {
        return "lobby";
    }

    @GetMapping("/games/{gameId}")
    public String renderChessBoard(@PathVariable Long gameId) {
        return "chessboard";
    }
}
