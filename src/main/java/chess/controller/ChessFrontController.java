package chess.controller;

import chess.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView renderChessBoard(@PathVariable Long gameId) {
        ModelAndView modelAndView = new ModelAndView("chessboard");
        modelAndView.addObject("gameId", gameId);
        return modelAndView;
    }
}
