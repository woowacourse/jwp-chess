package chess.controller;

import chess.dto.ChessBoardDto;
import chess.dto.StringChessBoardDto;
import chess.dto.response.ScoreResponseDto;
import chess.service.ChessService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChessController {
    private final ChessService chessService;

    public ChessController(final ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/start")
    public String start() {
        chessService.remove();
        chessService.makeRound();
        return makeNewGame();
    }

    @GetMapping("/reset")
    public String reset() {
        chessService.remove();
        chessService.resetRound();
        return makeNewGame();
    }

    @GetMapping("/chess")
    public String chess(final Model model) throws JsonProcessingException {
        ChessBoardDto chessBoard = chessService.chessBoardFromDB();
        StringChessBoardDto stringChessBoard = chessService.stringChessBoard(chessBoard);

        String jsonFormatChessBoard = chessService.jsonFormatChessBoard(stringChessBoard);
        model.addAttribute("jsonFormatChessBoard", jsonFormatChessBoard);
        String currentTurn = chessService.currentTurn();
        model.addAttribute("currentTurn", currentTurn);

        chessService.updateRound(chessBoard, currentTurn);

        ScoreResponseDto scoreResponseDto = chessService.scoreResponseDto();
        model.addAttribute("score", scoreResponseDto);
        return "chess";
    }

    private String makeNewGame() {
        ChessBoardDto chessBoard = chessService.chessBoard();
        StringChessBoardDto filteredChessBoard = chessService.filteredChessBoard(chessBoard);
        chessService.initialize(filteredChessBoard);
        return "redirect:/chess";
    }
}
