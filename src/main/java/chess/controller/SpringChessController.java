package chess.controller;

import chess.service.SpringChessGameService;
import chess.view.dto.ChessGameStatusDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpringChessController {

    private final SpringChessGameService chessGameService;

    public SpringChessController(SpringChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/")
    public String index(Model model) {
        ChessGameStatusDto latestGameStatus = chessGameService.findLatestChessGameStatus();
        model.addAttribute("status", latestGameStatus);
        return "index";
    }

}
