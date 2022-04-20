package chess.web.controller;

import chess.service.ChessService;
import chess.service.dto.BoardDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String renderIndex() {
        return "index";
    }

    @GetMapping("/board/{gameId}")
    public String renderBoard(@PathVariable int gameId, Model model) {
        BoardDto board = getRunningBoard(gameId);
        if (board == null) {
            return "redirect:status/" + gameId;
        }
        model.addAttribute("board", board);
        return "board";
    }

    private BoardDto getRunningBoard(int gameId) {
        if (chessService.isRunning(gameId) || chessService.isGameEmpty(gameId)) {
            return chessService.getBoard(gameId);
        }
        return null;
    }

    @GetMapping("/new-board/{gameId}")
    public String initBoard(@PathVariable int gameId) {
        chessService.initGame(gameId);
        return "redirect:../board/" + gameId;
    }
}
