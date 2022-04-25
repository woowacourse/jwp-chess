package chess.web.controller;

import chess.service.ChessService;
import chess.service.dto.BoardDto;
import chess.service.dto.GameResultDto;
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

    @GetMapping("/board/{gameId}")
    public String renderBoard(@PathVariable int gameId, Model model) {
        BoardDto board = getRunningBoard(gameId);
        if (board == null) {
            return "redirect:../status/" + gameId;
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
    public RedirectView initBoard(@PathVariable int gameId) {
        chessService.initGame(gameId);
        return new RedirectView("/board/" + gameId);
    }

    @PostMapping("/board")
    public RedirectView createGame(@RequestParam String name) {
        int gameId = chessService.createGame(name.trim());
        return new RedirectView("/board/" + gameId);
    }

    @PostMapping("/move/{gameId}")
    public RedirectView requestMove(@PathVariable int gameId, @RequestParam String from,
        @RequestParam String to) {
        chessService.move(gameId, from, to);
        return new RedirectView("/board/" + gameId);
    }

    @GetMapping("/status/{gameId}")
    public String renderStatus(@PathVariable int gameId, Model model) {
        GameResultDto status = chessService.getResult(gameId);
        chessService.endGame(gameId);
        model.addAttribute("status", status);
        return "result";
    }

    @GetMapping("/game-end/{gameId}")
    public RedirectView requestEndGame(@PathVariable int gameId) {
        chessService.endGame(gameId);
        return new RedirectView("/");
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleError(Exception ex, Model model) {
        model.addAttribute("exception", ex.getMessage());
        return "exception";
    }
}
