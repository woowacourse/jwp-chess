package chess.controller.spring;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MoveController {

    @GetMapping("/")
    public String moveToGamePage() {
        return "game.html";
    }

    @GetMapping("/result")
    public String moveToResultPage() {
        return "result.html";
    }
}
