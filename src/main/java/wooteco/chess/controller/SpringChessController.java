package wooteco.chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpringChessController {
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
