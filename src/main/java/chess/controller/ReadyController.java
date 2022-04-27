package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import chess.service.ChessService;

@Controller
public class ReadyController {
    private final ChessService chessService;

    public ReadyController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("games", chessService.getGameIDs());
        model.addAttribute("msg", "CLICK TO START! 😝");
        return "ready";
    }

    @PostMapping("/")
    public String delete(@RequestParam String gameID, Model model) {
        try {
            chessService.deleteGameByGameID(gameID);
        } catch (IllegalArgumentException e) {
            model.addAttribute("msg", e.getMessage());
        }
        model.addAttribute("games", chessService.getGameIDs());
        return "ready";
    }
}
