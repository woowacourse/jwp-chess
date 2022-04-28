package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChessWebController {

    @GetMapping("/")
    public String index() {
        return "lobby";
    }

    @GetMapping("/room/{roomId}")
    public String room() {
        return "room";
    }
}
