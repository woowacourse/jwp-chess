package chess.controller;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.*;
import chess.service.ChessService;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import spark.ModelAndView;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.sql.SQLException;
import java.util.*;

import static spark.Spark.get;
import static spark.Spark.post;

@Controller
public class SpringChessController {
    public static final Gson GSON = new Gson();

    private final ChessService chessService;

    public SpringChessController(final ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/start")
    public String start() throws SQLException {
        return makeNewGame();
    }

    @GetMapping("/reset")
    public String reset() throws SQLException {
        return makeNewGame();
    }

    @GetMapping("/chess")
    public String chess(final Model model) throws SQLException {
        Map<String, String> chessBoardFromDB = chessService.chessBoardFromDB();
        Map<Position, Piece> chessBoard = chessService.chessBoard(chessBoardFromDB);
        Map<String, String> stringChessBoard = chessService.stringChessBoard(chessBoard);
        PiecesDto piecesDto = chessService.piecesDto(chessBoard);

        String jsonFormatChessBoard = GSON.toJson(stringChessBoard);
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

    @PostMapping(value = "/move", produces = "application/json")
    public ResponseEntity move(@RequestBody MoveRequestDto moveRequestDto) throws SQLException {
        Queue<String> commands =
                new ArrayDeque<>(Arrays.asList("move", moveRequestDto.getSource(), moveRequestDto.getTarget()));
        try {
            chessService.executeRound(commands);
        } catch (RuntimeException runtimeException) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);

//            return "{\"status\":\"500\", \"message\":\"" + runtimeException.getMessage() + "\"}";
        }
        chessService.movePiece(moveRequestDto);
        return new ResponseEntity(HttpStatus.OK);
//        return "{\"status\":\"200\", \"message\":\"성공\"}";
    }

    @PostMapping("/turn")
    public void turn(@RequestBody TurnChangeRequestDto turnChangeRequestDto) throws SQLException {
        chessService.changeTurn(turnChangeRequestDto);
    }

    private String makeNewGame() throws SQLException {
        chessService.remove();
        chessService.makeRound();
        Map<Position, Piece> chessBoard = chessService.chessBoard();
        Map<String, String> filteredChessBoard = chessService.filteredChessBoard(chessBoard);
        chessService.initialize(filteredChessBoard);
        return "redirect:/chess";
    }
}
