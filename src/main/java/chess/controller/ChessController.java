package chess.controller;

import chess.model.GameResult;
import chess.model.dto.MoveDto;
import chess.model.dto.WebBoardDto;
import chess.service.ChessService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Controller
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Long> games = chessService.getAllGames();
        model.addAttribute("games", games);

        return "game-room";
    }

    @GetMapping("/enter/{gameId}")
    public String start(@PathVariable Long gameId, Model model) {
        model.addAttribute("gameId", gameId);

        return "index";
    }

    @PostMapping("/start/new")
    @ResponseBody
    public String startNewGame(@RequestBody String password) {
        Long gameId = chessService.createGame(password);
        return gameId.toString();
    }

    @GetMapping("/start/{gameId}")
    @ResponseBody
    public Map<String, String> startGame(@PathVariable Long gameId) {
        WebBoardDto board = chessService.continueGame(gameId);
        return board.getWebBoard();
    }

    @GetMapping(value = "/turn/{gameId}")
    @ResponseBody
    public String turn(@PathVariable Long gameId) {
        return chessService.getTurn(gameId);
    }

    @PostMapping("/move/{gameId}")
    @ResponseBody
    public Map<String, String> move(@PathVariable Long gameId, @RequestBody MoveDto moveCommand) {
        WebBoardDto board = chessService.move(gameId, moveCommand);
        return board.getWebBoard();
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

    @GetMapping("/restart/{gameId}")
    @ResponseBody
    public void restartGame(@PathVariable Long gameId) {
        chessService.restartGame(gameId);
    }

    @PostMapping("/delete/room/{gameId}")
    public void deleteGame(@PathVariable Long gameId, @RequestBody String password) {
        chessService.deleteGame(gameId, password);
    }
}
