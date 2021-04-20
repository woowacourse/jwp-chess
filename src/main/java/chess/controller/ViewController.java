package chess.controller;

import chess.dto.*;
import chess.service.ChessService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    private final ChessService chessService;

    public ViewController(final ChessService chessService) {
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
        StringChessBoardDto dbChessBoard = chessService.dbChessBoard();
        ChessBoardDto chessBoard = chessService.chessBoard(dbChessBoard);
        StringChessBoardDto stringChessBoard = chessService.stringChessBoard(chessBoard);
        PiecesDto piecesDto = chessService.piecesDto(chessBoard);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonFormatChessBoard = objectMapper.writeValueAsString(stringChessBoard.getStringChessBoard());
        model.addAttribute("jsonFormatChessBoard", jsonFormatChessBoard);

        chessService.updateRound(piecesDto);

        String currentTurn = chessService.currentTurn();
        model.addAttribute("currentTurn", currentTurn);

        chessService.changeRoundState(currentTurn);

        PlayerDto playerDto = chessService.playerDto();
        ScoreDto scoreDto = chessService.scoreDto(playerDto);
        chessService.changeRoundToEnd(playerDto);

        model.addAttribute("whiteScore", scoreDto.getWhiteScore());
        model.addAttribute("blackScore", scoreDto.getBlackScore());
        return "chess";
    }

    private String makeNewGame() {
        ChessBoardDto chessBoard = chessService.chessBoard();
        StringChessBoardDto filteredChessBoard = chessService.filteredChessBoard(chessBoard);
        chessService.initialize(filteredChessBoard);
        return "redirect:/chess";
    }
}
