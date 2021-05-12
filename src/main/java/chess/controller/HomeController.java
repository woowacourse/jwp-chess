package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    public HomeController() {
    }

    @GetMapping("/")
    public String lobby() {
        return "lobby";
    }

    @GetMapping("/room/{id}")
    public String room(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        return "./index";
    }
}
