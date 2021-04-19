package chess.controller.spring;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MoveController {

    @GetMapping("/")
    public String moveToMainPage() {
        return "index";
    }

    @GetMapping("/chessgame/{id}")
    public String moveToGamePage(@PathVariable String id, Model model) {
        model.addAttribute("roomId", id);
        return "game";
    }

    @GetMapping("/result/{id}")
    public String moveToResultPage(@PathVariable String id, Model model) {
        model.addAttribute("roomId", id);
        return "result";
    }
}
