package chess.controller;

import chess.service.ChessRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import chess.domain.game.GameState;
import chess.dto.GameStateResponse;

@Controller
public class SpringViewChessController {

    private final ChessRoomService gameService;

    @Autowired
    public SpringViewChessController(ChessRoomService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/")
    public String showIndex() {
        return "index";
    }

    @GetMapping(path = "/main")
    public String showGame(Model model, @RequestParam("room_id") int roomId) {
        GameState state = gameService.readGameState(roomId);
        GameStateResponse response = GameStateResponse.of(state);
        model.addAttribute("response", response);
        return "game";
    }
}
