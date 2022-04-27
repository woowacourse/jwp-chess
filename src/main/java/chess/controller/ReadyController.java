package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
        return "ready";
    }
}
