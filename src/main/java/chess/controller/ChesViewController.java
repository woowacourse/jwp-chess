package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChesViewController {

    @GetMapping("/")
    public String main() {
        return "/main.html";
    }

    @GetMapping("/rooms/{roomId}")
    public String room() {
        return "/room.html";
    }
}
