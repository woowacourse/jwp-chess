package wooteco.chess.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import wooteco.chess.service.ChessService;

//@Controller
public class ChessController {

    private ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/start")
    public String startGame(Model model) {
        chessService.start();
        model.addAllAttributes(chessService.makeStartResponse());
        return "chessGameStart";
    }
}
