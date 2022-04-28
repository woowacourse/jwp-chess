package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import chess.dto.ChessGameDto;
import chess.domain.GameResult;
import chess.domain.piece.Color;
import chess.service.ChessService;

@Controller
public class ResultController {
    private final ChessService chessService;

    public ResultController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/results")
    public String showResult(@ModelAttribute ChessGameDto chessGameDto, Model model) {

        GameResult gameResult = chessService.getGameResult(chessGameDto);
        model.addAttribute("whiteScore", gameResult.calculateScore(Color.WHITE));
        model.addAttribute("blackScore", gameResult.calculateScore(Color.BLACK));
        return "status";
    }
}
