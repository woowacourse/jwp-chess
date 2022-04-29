package chess.web.controller;

import chess.web.service.ChessService;
import java.util.NoSuchElementException;
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
        model.addAttribute("rooms", chessService.getRooms());
        return "index";
    }

    @GetMapping("/chess/{boardId}")
    public String chess(@PathVariable Long boardId) {
        try {
            chessService.loadGame(boardId);
        } catch (NoSuchElementException exception) {
            return "nochess";
        }
        return "chess";
    }
}
