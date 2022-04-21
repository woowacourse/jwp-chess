package chess.controller;

import chess.domain.ChessGameService;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChessSpringController {

    private final ChessGameService chessGameService;
    private final Gson gson = new Gson();

    public ChessSpringController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/")
    public String enter() {
        chessGameService.init();
        return "index";
    }
}
