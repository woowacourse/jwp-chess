package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HtmlController {

    @GetMapping("/")
    public String main() {
        return "main.html";
    }

    @GetMapping("/chess")
    public String chessTemplate() {
        return "/chess.html";
    }
}
