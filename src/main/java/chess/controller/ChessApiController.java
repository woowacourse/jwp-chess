package chess.controller;

import chess.model.GameResult;
import chess.model.dto.MoveDto;
import chess.model.dto.WebBoardDto;
import chess.service.ChessService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
public class ChessApiController {

    private final ChessService chessService;

    public ChessApiController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("/start/new")
    public String startNewGame(@RequestBody String password) {
        Long gameId = chessService.createGame(password);
        return gameId.toString();
    }

    @GetMapping("/start/{gameId}")
    public Map<String, String> startGame(@PathVariable Long gameId) {
        WebBoardDto board = chessService.continueGame(gameId);
        return board.getWebBoard();
    }

    @GetMapping(value = "/turn/{gameId}")
    public String turn(@PathVariable Long gameId) {
        return chessService.getTurn(gameId);
    }

    @PostMapping("/move/{gameId}")
    public Map<String, String> move(@PathVariable Long gameId, @RequestBody MoveDto moveCommand) {
        WebBoardDto board = chessService.move(gameId, moveCommand);
        return board.getWebBoard();
    }

    @GetMapping("/king/dead/{gameId}")
    public boolean kingDead(@PathVariable Long gameId) {
        return chessService.isKingDead(gameId);
    }

    @GetMapping("/status/{gameId}")
    public GameResult status(@PathVariable Long gameId) {
        return chessService.getResult(gameId);
    }

    @PostMapping("/exit/{gameId}")
    public void exit(@PathVariable Long gameId) {
        chessService.exitGame(gameId);
    }

    @GetMapping("/restart/{gameId}")
    public void restartGame(@PathVariable Long gameId) {
        chessService.restartGame(gameId);
    }

    @DeleteMapping("/delete/room/{gameId}")
    public void deleteGame(@PathVariable Long gameId, @RequestBody String password) {
        chessService.deleteGame(gameId, password);
    }
}
