package chess.web.controller;

import chess.service.ChessService;
import chess.service.dto.request.CreateGameRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String renderIndex(Model model) {
        model.addAttribute("games", chessService.getAllGames());
        return "index";
    }

    @GetMapping("/game/{gameId}")
    public String renderBoard(@PathVariable Integer gameId) {
        return "board";
    }

    @PostMapping("/game")
    public String createGame(CreateGameRequest createGameRequest) {
        String name = createGameRequest.getName();
        String password = createGameRequest.getPassword();
        chessService.createGame(name, password);
        return "redirect:/";
    }
}
