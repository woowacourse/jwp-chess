package chess.web.controller;

import chess.web.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChessWebController {

    private final ChessService chessService;

    public ChessWebController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("boards", chessService.getBoards());
        return "index";
    }

    @GetMapping("/chess/{boardId}")
    public String chess(@PathVariable Long boardId) {
        chessService.loadGame(boardId);
        return "chess";
    }
}
