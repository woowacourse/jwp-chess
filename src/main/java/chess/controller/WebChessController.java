package chess.controller;

import chess.dto.ScoreDto;
import chess.service.ChessGameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebChessController {

    private static final String CHESS_GAME_URL = "chessGame";
    private static final String START_URL = "start";
    private final ChessGameService service;

    public WebChessController(ChessGameService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String main() {
        return START_URL;
    }

    @GetMapping("/game")
    public String game(Model model) {
        service.init();
        model.addAttribute("pieces", service.getPiecesByUnicode());
        return CHESS_GAME_URL;
    }

    @GetMapping("/start")
    public String start(Model model) {
        service.start();
        model.addAttribute("pieces", service.getPiecesByUnicode());
        return CHESS_GAME_URL;
    }

    @GetMapping("/end")
    public String end(Model model) {
        service.end();
        model.addAttribute("pieces", service.getPiecesByUnicode());
        return CHESS_GAME_URL;
    }

    @GetMapping("/restart")
    public String restart(Model model) {
        service.restart();
        model.addAttribute("pieces", service.getPiecesByUnicode());
        return CHESS_GAME_URL;
    }

    @GetMapping("/save")
    public String save(Model model) {
        service.save();
        model.addAttribute("pieces", service.getPiecesByUnicode());
        return CHESS_GAME_URL;
    }

    @GetMapping("/status")
    public String status(Model model) {
        ScoreDto score = service.status();
        model.addAttribute("pieces", service.getPiecesByUnicode());
        model.addAttribute("score", score);
        return CHESS_GAME_URL;
    }

    @PostMapping("/move")
    public String move(@RequestParam String from, @RequestParam String to, Model model) {
        service.move(from, to);
        model.addAttribute("pieces", service.getPiecesByUnicode());
        return CHESS_GAME_URL;
    }

    @ExceptionHandler(RuntimeException.class)
    private String handelException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "chessGame";
    }
}
