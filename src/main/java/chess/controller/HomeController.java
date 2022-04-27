package chess.controller;

import chess.service.ChessGameService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class HomeController {

    private final ChessGameService chessGameService;

    public HomeController(final ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/")
    public String getIndexPage(final Model model) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("chess_games", chessGameService.loadAllChessGames());
        model.addAllAttributes(attributes);
        return "index";
    }

    @PostMapping("/game")
    public RedirectView createChessGame(final @RequestParam String name, final @RequestParam String password) {
        long chessGameId = chessGameService.createChessGame(name, password);
        return new RedirectView("/games/" + chessGameId);
    }

    @PostMapping("/delete/{chessGameId}")
    public RedirectView deleteChessGame(final @PathVariable long chessGameId, final @RequestParam String password) {
        chessGameService.deleteChessGame(chessGameId, password);
        return new RedirectView("/");
    }
}
