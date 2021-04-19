package chess.controller;

import chess.dto.ChessGameStatusDto;
import chess.service.ChessGameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChessController {

    private final ChessGameService chessGameService;

    public ChessController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/")
    public String index(Model model) {
        ChessGameStatusDto chessGameStatus = chessGameService.findLatestChessGameStatus();
        model.addAttribute("chessGameStatus", chessGameStatus);
        return "index";
    }

}
