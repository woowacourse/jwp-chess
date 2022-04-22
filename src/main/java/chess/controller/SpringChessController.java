package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chess-game")
public class SpringChessController {

    @GetMapping()
    public String init() {
        return "index";
    }


}
