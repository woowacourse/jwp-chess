package chess.controller;

import chess.model.GameResult;
import chess.model.dto.MoveDto;
import chess.model.dto.WebBoardDto;
import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @GetMapping("/start")
    @ResponseBody
    public Map<String, String> start() {
        WebBoardDto board = chessService.start();
        return board.getWebBoard();
    }

    @PostMapping("/move")
    @ResponseBody
    public Map<String, String> move(@RequestBody MoveDto moveCommand) {
        WebBoardDto board = chessService.move(moveCommand);
        return board.getWebBoard();
    }

    @GetMapping("/turn")
    @ResponseBody
    public String turn() {
        return chessService.getTurn();
    }

    @GetMapping("/king/dead")
    @ResponseBody
    public boolean kingDead() {
        return chessService.isKingDead();
    }

    @GetMapping("/status")
    @ResponseBody
    public GameResult status() {
        return chessService.getResult();
    }
}
