package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@Controller
public class ViewController {

    @GetMapping("/")
    public String index(HttpSession session) {
        session.removeAttribute("roomId");
        return "index";
    }

    @GetMapping("/game/{roomId}")
    public String game(@PathVariable Long roomId, HttpSession session) {
        session.setAttribute("roomId", roomId);
        return "game";
    }

    @GetMapping("/game/create")
    public String createGame() {
        return "newGame";
    }
}
