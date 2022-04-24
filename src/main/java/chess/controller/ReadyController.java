package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import chess.service.ChessService;

@Controller
public class ReadyController {
    private final ChessService chessService;

    public ReadyController(final ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String index() {
        return "ready";
    }
}
