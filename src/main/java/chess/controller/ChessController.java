package chess.controller;

import chess.dto.ChessGameStatusDto;
import chess.service.SpringChessGameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChessController {

    private final SpringChessGameService chessGameService;

    public ChessController(SpringChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/")
    public String index(Model model) {
        ChessGameStatusDto latestGameStatus = chessGameService.findLatestChessGameStatus();
        model.addAttribute("status", latestGameStatus);
        return "index";
    }

}
