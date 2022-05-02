package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import chess.dto.ChessGameRequest;
import chess.domain.piece.Color;
import chess.service.ChessService;

@Controller
public class ResultController {
    private final ChessService chessService;

    public ResultController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/results")
    public String showResult(@ModelAttribute ChessGameRequest chessGameRequest, Model model) {

        model.addAttribute("whiteScore", chessService.calculateScore(chessGameRequest, Color.WHITE));
        model.addAttribute("blackScore", chessService.calculateScore(chessGameRequest, Color.BLACK));
        return "status";
    }
}
