package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ChessViewController {

    @GetMapping("")
    public String home() {
        return "home";
    }

    @GetMapping("/game/{id}")
    public String board() {
        return "board";
    }
}
