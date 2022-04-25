package chess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import chess.domain.game.GameState;
import chess.dto.GameStateResponse;
import chess.service.GameService;

@Controller
public class SpringViewChessController {

    private final GameService gameService;

    @Autowired
    public SpringViewChessController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/")
    public String showIndex() {
        return "index";
    }

    @GetMapping(path = "/main")
    public String showGame(Model model, @RequestParam("room_name") String roomName) {
        GameState state = gameService.readGameState(roomName);
        GameStateResponse response = GameStateResponse.of(state);
        model.addAttribute("response", response);
        return "game";
    }
}
