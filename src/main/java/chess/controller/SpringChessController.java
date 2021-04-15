package chess.controller;

import chess.service.SpringChessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SpringChessController {
    private final SpringChessService springChessService;

    public SpringChessController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    @GetMapping("/")
    public String mainPage() {
        return "main";
    }

    @PostMapping("/start")
    public String startGame(@RequestParam("room") String id, Model model) {
        model.addAttribute("roomId", id);
        return "index";
    }

    @GetMapping("/clear/{id}")
    public String clear(@PathVariable String id) {
        springChessService.deleteRoom(id);
        return "redirect:/";
    }
}
