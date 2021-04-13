package chess.controller;

import chess.service.ChessGameService;
import chess.view.dto.ChessGameStatusDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpringChessController {

    private final ChessGameService chessGameService;

    public SpringChessController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/")
    public String index(Model model) {
        ChessGameStatusDto latestGameStatus = chessGameService.findLatestChessGameStatus();
        model.addAttribute("status", latestGameStatus);
        return "index";
    }
}
