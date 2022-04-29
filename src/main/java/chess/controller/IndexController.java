package chess.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import chess.service.ChessService;

@Controller
public class IndexController {

    private final ChessService chessService;

    public IndexController(final ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String index(final Model model) {
        final Map<Long, Boolean> games = chessService.listGames();
        model.addAttribute("games", games);
        return "index";
    }
}