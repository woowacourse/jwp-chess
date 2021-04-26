package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping("/")
    public String start() {
        return "main.html";
    }

    @GetMapping("/rooms/{roomNumber}")
    public String moveRoom() {
        return "/chess.html";
    }
}
