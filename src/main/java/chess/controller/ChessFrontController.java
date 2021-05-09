package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ChessFrontController {
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
