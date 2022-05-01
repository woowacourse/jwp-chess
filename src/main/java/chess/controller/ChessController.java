package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChessController {

    @GetMapping("/")
    public String index() {
        return "/lobby.html";
    }

    @GetMapping("/rooms/{roomId}")
    public String room(@PathVariable Long roomId) {
        return "/room.html";
    }
}
