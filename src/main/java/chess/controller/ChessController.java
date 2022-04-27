package chess.controller;

import chess.model.GameResult;
import chess.model.dto.GameInfoDto;
import chess.model.dto.MoveDto;
import chess.model.dto.RoomDto;
import chess.model.dto.WebBoardDto;
import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
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

    @PostMapping("/new")
    @ResponseBody
    public Map<String, String> startNewGame(@RequestBody RoomDto roomDto) {
        WebBoardDto board = chessService.start(roomDto);
        return board.getWebBoard();
        // 생각해볼 부분: ResponseEntity의 사용
    }

    @GetMapping("/games")
    @ResponseBody
    public List<GameInfoDto> getAllGames() {
        return chessService.getAllGames();
    }

    @GetMapping("/start")
    @ResponseBody
    public Map<String, String> start() {
//        WebBoardDto board = chessService.start();
//        return board.getWebBoard();
        return null;
    }

    @PostMapping("/move")
    @ResponseBody
    public Map<String, String> move(@RequestBody MoveDto moveCommand) {
        WebBoardDto board = chessService.move(moveCommand);
        return board.getWebBoard();
    }

    @GetMapping(value = "/turn")
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

    @PostMapping("/exit")
    @ResponseBody
    public void exit() {
        chessService.exitGame();
    }
}
