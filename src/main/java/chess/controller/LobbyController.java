package chess.controller;

import chess.service.ChessGameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class LobbyController {

    private final ChessGameService chessGameService;

    public LobbyController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/")
    public String lobby(Model model) {
        model.addAttribute("chess-games", chessGameService.findAll());
        return "index";
    }

    @PostMapping("/create-chess-game")
    public String createChessGame(@RequestParam String name) {
        chessGameService.create(name);
        return "redirect:/";
    }
}
