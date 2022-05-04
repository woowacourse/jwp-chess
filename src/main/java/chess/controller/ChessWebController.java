package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChessWebController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/rooms/{roomId}")
    public String room(@PathVariable final int roomId) {
        return "board";
    }
}
