package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class SpringChessController {

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/rooms/{roomName}")
    public String enterRoom(@PathVariable String roomName, Model model) {
        model.addAttribute("roomName", roomName);
        return "chess";
    }
}
