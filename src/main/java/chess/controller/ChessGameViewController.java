package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChessGameViewController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/play")
    public String play() {
        return "play";
    }

    @GetMapping("/games")
    public String games() {
        return "games";
    }
}
