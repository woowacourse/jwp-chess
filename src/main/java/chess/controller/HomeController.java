package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    public HomeController() {
    }

    @GetMapping("/")
    public String lobby() {
        return "lobby";
    }

    @GetMapping("/games/{id}")
    public String initBoard() {
        return "./index";
    }
}
