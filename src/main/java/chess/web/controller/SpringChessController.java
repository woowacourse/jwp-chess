package chess.web.controller;

import chess.service.ChessService;
import chess.service.dto.BoardDto;
import chess.service.dto.GameResultDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String renderBoard(@PathVariable int gameId, Model model) {
        if (chessService.isEnd(gameId)) {
            return "redirect:../status/" + gameId;
        }
        BoardDto board = chessService.getBoard(gameId);
        model.addAttribute("board", board);
        return "board";
    }

    @PostMapping("/game")
    public String createGame(@RequestParam String name, @RequestParam String password) {
        chessService.createGame(name.trim(), password);
        return "redirect:/";
    }

    @GetMapping("/status/{gameId}")
    public String renderStatus(@PathVariable int gameId, Model model) {
        GameResultDto status = chessService.getResult(gameId);
        chessService.endGame(gameId);
        model.addAttribute("status", status);
        return "result";
    }
}
