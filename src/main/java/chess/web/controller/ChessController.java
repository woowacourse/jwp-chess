package chess.web.controller;

import chess.domain.state.StateType;
import chess.web.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping
    public String showIndex(final Model model) {
        model.addAttribute("games", chessService.getAllGame());
        return "index";
    }

    @PostMapping
    public String createGame(@RequestParam String title, @RequestParam String password) {
        int gameId = chessService.newGame(title, password);
        return "redirect:/game/" + gameId;
    }

    @GetMapping("/game/{gameId}")
    public String showGame(@PathVariable int gameId, final Model model) {
        if (chessService.getCurrentChessSate(gameId) == StateType.END) {
            return "redirect:/game/" + gameId + "/result";
        }
        model.addAttribute("chessStatus", chessService.getBoard(gameId));

        return "game";
    }

    @GetMapping("/game/{gameId}/result")
    public String showResult(@PathVariable int gameId, final Model model) {
        model.addAttribute("result", chessService.getChessResult(gameId));
        return "result";
    }

    @GetMapping("/game/{gameId}/restart")
    public String restartGame(@PathVariable int gameId) {
        chessService.restart(gameId);
        return "redirect:/game/" + gameId;
    }
}
