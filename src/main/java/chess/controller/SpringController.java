package chess.controller;

import chess.service.ChessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpringController {

    private final ChessService chessService;

    @Autowired
    public SpringController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String home() {
        return "index.html";
    }
}
