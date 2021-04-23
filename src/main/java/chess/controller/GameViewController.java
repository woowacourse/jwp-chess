package chess.controller;

import chess.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/games")
@Controller
public class GameViewController {

    final GameService gameService;

    public GameViewController(chess.service.GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/{id}")
    public String getGameById() {
        return "chess";
    }
}
