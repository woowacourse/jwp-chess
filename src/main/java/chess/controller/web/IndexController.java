package chess.controller.web;

import chess.service.ChessGameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@ControllerAdvice
public class IndexController {

    private final ChessGameService service;

    public IndexController(ChessGameService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("gameList", service.findGameList());
        return "index";
    }

    @PostMapping("/createGame")
    public String createGame(@RequestParam String name, @RequestParam String password) {
        service.create(name, password);
        return "redirect:/";
    }

}
