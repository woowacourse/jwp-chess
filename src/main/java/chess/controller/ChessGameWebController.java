package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChessGameWebController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/new-game")
    public String newGame() {
        return "new-game";
    }

    @GetMapping("/start-game")
    public String gamePage() {
        return "game";
    }

    @GetMapping("/load/{gameId}")
    public String loadGame(@PathVariable int gameId) {
        return "game";
    }
}
