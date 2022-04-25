package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/game")
    public RedirectView redirectGame(@RequestParam("gameId") String gameId) {
        return new RedirectView("/game/" + gameId);
    }

    @GetMapping("/game/{gameId}")
    public String joinGame() {
        return "/game.html";
    }
}
