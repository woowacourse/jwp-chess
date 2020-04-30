package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/room/{roomId}")
    public String initRoom() {
        return "room";
    }

    @GetMapping("/room/{roomId}/game/{gameId}")
    public String game() {
        return "game";
    }

    @GetMapping("/result")
    public String result() {
        return "result";
    }
}
