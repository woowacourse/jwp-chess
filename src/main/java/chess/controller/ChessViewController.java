package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chess-game")
public class ChessViewController {

    @GetMapping
    public String showRooms() {
        return "rooms";
    }

    @GetMapping("/index")
    public String init() {
        return "index";
    }
}
