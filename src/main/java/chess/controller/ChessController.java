package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class ChessController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/{id}")
    public String game(@PathVariable Long id) {
        return "game";
    }
}
