package chess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChessController {

    @Autowired
    public ChessController() {
    }

    @GetMapping("/")
    public String lobby() {
        return "lobby";
    }

    @GetMapping("/{gameId}")
    public String initBoard() {
        return "index";
    }
}
