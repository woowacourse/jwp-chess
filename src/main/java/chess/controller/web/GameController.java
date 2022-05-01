package chess.controller.web;

import chess.dto.ScoreDto;
import chess.service.ChessGameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller()
@RequestMapping("/game")
public class GameController {

    private static final String CHESS_GAME_URL = "chessGame";

    private final ChessGameService service;

    public GameController(ChessGameService service) {
        this.service = service;
    }

    @GetMapping("/{gameId}")
    public String game(@PathVariable long gameId, Model model) {
        model.addAttribute("pieces", service.getBoardByUnicode(gameId));
        model.addAttribute("gameId", gameId);

        return CHESS_GAME_URL;
    }

    @PostMapping("/{gameId}/start")
    public String start(@PathVariable long gameId) {
        service.start(gameId);
        return "redirect:/game/" + gameId;
    }

    @PostMapping("/{gameId}/end")
    public String end(@PathVariable long gameId) {
        service.end(gameId);
        return "redirect:/game/" + gameId;
    }

    @GetMapping("/{gameId}/status")
    public String status(@PathVariable long gameId, Model model) {
        ScoreDto score = service.status(gameId);
        model.addAttribute("pieces", service.getBoardByUnicode(gameId));
        model.addAttribute("score", score);
        return CHESS_GAME_URL;
    }

    @PostMapping("/{gameId}/move")
    public String move(@PathVariable long gameId, @RequestParam String from, @RequestParam String to) {
        service.move(gameId, from, to);
        return "redirect:/game/" + gameId;
    }

    @PostMapping("/{gameId}/delete")
    public String delete(@PathVariable long gameId, @RequestParam String password) {
        service.delete(gameId, password);
        return "redirect:/";
    }

    @ExceptionHandler(RuntimeException.class)
    private String handelException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error";
    }
}
