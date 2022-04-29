package chess.controller;

import chess.service.ChessGameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


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

}
