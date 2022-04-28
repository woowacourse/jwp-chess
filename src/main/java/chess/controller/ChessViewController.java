package chess.controller;

import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChessViewController {

    private final ChessService chessService;

    public ChessViewController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("games", chessService.getAllGames());

        return "index";
    }

    @GetMapping("/enter/{gameId}")
    public String start(@PathVariable Long gameId, Model model) {
        model.addAttribute("gameId", gameId);

        return "game-room";
    }
}
