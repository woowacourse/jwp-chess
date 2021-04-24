package chess.web.controller;

import chess.domain.game.dao.ChessGameDao;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private final ChessGameDao chessGameDAO;

    public IndexController(ChessGameDao chessGameDAO) {
        this.chessGameDAO = chessGameDAO;
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String index(Model model) {
        model.addAttribute("games", chessGameDAO.findActiveGames());
        return "lobby";
    }
}
