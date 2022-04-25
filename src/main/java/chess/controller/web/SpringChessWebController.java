package chess.controller.web;

import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpringChessWebController {

    private final ChessService chessService;

    public SpringChessWebController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String root() {
        return "index";
    }

    @GetMapping("/index")
    public String index() {
        return "redirect:/";
    }

    @GetMapping("/new-game")
    public String playNewGame() {
        return "game";
    }

    @GetMapping("/load-game")
    public String loadGame() {

        if (!chessService.isExistGame()) {
            return "redirect:/";
        }

        return "game";
    }
}
