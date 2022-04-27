package chess.controller;

import chess.model.GameResult;
import chess.model.dto.MoveDto;
import chess.model.dto.WebBoardDto;
import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Controller
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/start")
    @ResponseBody
    public Map<String, String> startNewGame(@RequestBody String password) {
        WebBoardDto board = chessService.createGame(password);
        return board.getWebBoard();
    }

    @GetMapping("/start/{gameId}")
    @ResponseBody
    public Map<String, String> startGame(@PathVariable Long gameId) {
        WebBoardDto board = chessService.continueGame(gameId);
        return board.getWebBoard();
    }

    @PostMapping("/move/{gameId}")
    @ResponseBody
    public Map<String, String> move(@PathVariable Long gameId, @RequestBody MoveDto moveCommand) {
        WebBoardDto board = chessService.move(gameId, moveCommand);
        return board.getWebBoard();
    }

    @GetMapping(value = "/turn/{gameId}")
    @ResponseBody
    public String turn(@PathVariable Long gameId) {
        return chessService.getTurn(gameId);
    }

    @GetMapping("/king/dead/{gameId}")
    @ResponseBody
    public boolean kingDead(@PathVariable Long gameId) {
        return chessService.isKingDead(gameId);
    }

    @GetMapping("/status/{gameId}")
    @ResponseBody
    public GameResult status(@PathVariable Long gameId) {
        return chessService.getResult(gameId);
    }

    @PostMapping("/exit/{gameId}")
    @ResponseBody
    public void exit(@PathVariable Long gameId) {
        chessService.exitGame(gameId);
    }
}
