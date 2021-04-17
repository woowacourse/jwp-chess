package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChessWebController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/chess")
    public String chess() {
        return "chess";
    }
}
