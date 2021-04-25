package chess.web.controller;

import chess.domain.game.dto.ChessGameDto;
import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chess")
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/{gameId}")
    public String gameDetailView(@PathVariable String gameId, Model model) {
        ChessGameDto chessGameDTO = chessService.findGameById(gameId);
        model.addAttribute("chessGame", chessGameDTO);
        return "game";
    }
}
