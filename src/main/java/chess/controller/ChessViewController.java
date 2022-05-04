package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import chess.domain.game.GameState;
import chess.dto.GameStateResponse;
import chess.service.GameService;

@Controller
public class ChessViewController {

    public static final String MAIN_PATH_FORMAT = "/main/%d";
    public static final String ROOT_PATH = "/";

    private final GameService service;

    public ChessViewController(GameService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String showIndex() {
        return "index";
    }

    @GetMapping(path = "/main/{roomId}")
    public String showGame(Model model, @PathVariable Long roomId) {
        GameState state = service.readGameState(roomId);
        GameStateResponse response = GameStateResponse.of(state);
        model.addAttribute("response", response);
        return "game";
    }
}
