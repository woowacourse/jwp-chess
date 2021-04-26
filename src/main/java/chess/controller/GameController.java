package chess.controller;

import chess.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    public GameController(final GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/{id}")
    public String loadGameById() {
        return "chess";
    }

}
