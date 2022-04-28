package chess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import chess.domain.game.GameState;
import chess.dto.GameStateResponse;
import chess.service.GameService;

@Controller
public class SpringViewChessController {

    public static final String MAIN_PATH_FORMAT = "/main/%d";
    public static final String ROOT_PATH = "/";

    private final GameService gameService;

    @Autowired
    public SpringViewChessController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/")
    public String showIndex() {
        return "index";
    }

    @GetMapping(path = "/main/{roomId}")
    public String showGame(Model model, @PathVariable Long roomId) {
        GameState state = gameService.readGameState(roomId);
        GameStateResponse response = GameStateResponse.of(state);
        model.addAttribute("response", response);
        return "game";
    }

}
