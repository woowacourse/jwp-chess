package chess.controller;

import chess.model.GameResult;
import chess.model.dto.*;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public String game(@PathVariable Long id) {
        return "game";
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
    public ResponseEntity<GameInfosDto> getAllGames() {
        return ResponseEntity.ok(chessService.getAllGames());
    }

    @GetMapping("/game/{id}")
    @ResponseBody
    public Map<String, String> getExistedGame(@PathVariable Long id) {
        WebBoardDto board = chessService.getBoardByGameId(id);
        return board.getWebBoard();
    }

    @DeleteMapping("/game/{id}")
    @ResponseBody
    public void deleteGame(@PathVariable Long id) {
        chessService.deleteByGameId(id);
    }

    @GetMapping("/start")
    @ResponseBody
    public Map<String, String> start() {
//        WebBoardDto board = chessService.start();
//        return board.getWebBoard();
        return null;
    }

    @PostMapping("/game/{id}/move")
    @ResponseBody
    public Map<String, String> move(@PathVariable Long id, @RequestBody MoveDto moveCommand) {
        WebBoardDto board = chessService.move(moveCommand, id);
        return board.getWebBoard();
    }

    @GetMapping(value = "/game/{id}/turn")
    @ResponseBody
    public String turn(@PathVariable Long id) {
        return chessService.getTurn(id);
    }

    @GetMapping("/game/{id}/dead")
    @ResponseBody
    public boolean isKingDead(@PathVariable Long id) {
        return chessService.isKingDead(id);
    }

    @GetMapping("/game/{id}/status")
    @ResponseBody
    public GameResult status(@PathVariable Long id) {
        return chessService.getResult(id);
    }

    //필요없는 메서드
//    @PostMapping("/exit")
//    @ResponseBody
//    public void exit() {
//        chessService.exitGame();
//    }
}
