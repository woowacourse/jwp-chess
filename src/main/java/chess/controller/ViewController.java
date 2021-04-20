package chess.controller;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.*;
import chess.service.ChessService;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class ViewController {
    public static final Gson GSON = new Gson();

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
    public String chess(final Model model) {
        StringChessBoardDto dbChessBoard = chessService.dbChessBoard();
        ChessBoardDto chessBoard = chessService.chessBoard(dbChessBoard);
        StringChessBoardDto stringChessBoard = chessService.stringChessBoard(chessBoard);
        PiecesDto piecesDto = chessService.piecesDto(chessBoard);

        String jsonFormatChessBoard = GSON.toJson(stringChessBoard.getStringChessBoard());
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
