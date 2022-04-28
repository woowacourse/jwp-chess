package chess.controller;

import chess.model.GameResult;
import chess.model.dto.MoveDto;
import chess.model.dto.RoomDto;
import chess.model.dto.WebBoardDto;
import chess.service.ChessService;
import java.util.Map;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ChessApiController {

    private final ChessService chessService;

    public ChessApiController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("/start/new")
    public String startNewGame(@RequestBody RoomDto roomDto) {
        Long gameId = chessService.createGame(roomDto.getTitle(), roomDto.getPassword());
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
