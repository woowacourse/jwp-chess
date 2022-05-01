package chess.web.controller;

import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class SpringChessController {

    private final ChessService chessService;

    public SpringChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String renderIndex(Model model) {
        model.addAttribute("games", chessService.getAllGames());
        return "index";
    }

    @GetMapping("/game/{gameId}")
    public String gameRoom(@PathVariable Long gameId) {
        boolean gameExist = chessService.existsGameById(gameId);
        if (!gameExist) {
            return "error/404";
        }
        return "board";
    }

    @PostMapping("/game")
    public RedirectView createGame(@RequestParam String name, @RequestParam String password) {
        Long gameId = chessService.createGame(name.trim(), password);
        return new RedirectView("/game/" + gameId);
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleError(Exception ex, Model model) {
        model.addAttribute("exception", ex.getMessage());
        return "exception";
    }
}
