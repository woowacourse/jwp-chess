package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ChessController {

    @GetMapping()
    public String chess() {
        return "chess";
    }

    @PostMapping("/write")
    public String write() {
        return "write";
    }

    @GetMapping("/list")
    public String list() {
        return "list";
    }

    @GetMapping("/game")
    public String board() {
        return "board";
    }
}
