package chess.controller.web;

import chess.service.ChessGameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{gameId}/delete")
    public String delete(@PathVariable int gameId, @RequestParam String password) {
        service.delete(gameId, password);
        return "redirect:/";
    }

}
