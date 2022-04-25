package chess.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import chess.service.GameService;

@Controller
public class IndexController {

    private final GameService gameService;

    public IndexController(final GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/")
    public String index(final Model model) {
        final Map<Long, Boolean> games = gameService.listGames();
        model.addAttribute("games", games);
        return "index";
    }
}