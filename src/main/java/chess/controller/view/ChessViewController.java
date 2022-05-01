package chess.controller.view;

import chess.service.ChessService;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChessViewController {

    private final ChessService chessService;

    public ChessViewController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String root() {
        return "index";
    }

    @GetMapping("/index")
    public String index() {
        return "redirect:/";
    }

    @GetMapping("/new-game")
    public String playNewGame(Model model) {
        model.addAttribute("isNewGame", true);
        return "game";
    }

    @GetMapping("/load-game")
    public String loadGame(HttpSession session, Model model) {

        if (!chessService.isExistGame()) {
            return "redirect:/";
        }

        model.addAttribute("isNewGame", false);
        chessService.loadLastGame(session);

        return "game";
    }
}
