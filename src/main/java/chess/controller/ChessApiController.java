package chess.controller;

import chess.model.GameResult;
import chess.model.dto.MoveDto;
import chess.model.dto.RoomDto;
import chess.model.dto.WebBoardDto;
import chess.service.ChessBoardService;
import chess.service.ChessGameService;
import chess.service.ChessMoveService;
import java.util.Map;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessApiController {

    private final ChessGameService chessGameService;
    private final ChessBoardService chessBoardService;
    private final ChessMoveService chessMoveService;

    public ChessApiController(ChessGameService chessGameService,
                              ChessBoardService chessBoardService,
                              ChessMoveService chessMoveService) {
        this.chessGameService = chessGameService;
        this.chessBoardService = chessBoardService;
        this.chessMoveService = chessMoveService;
    }

    @PostMapping("/board/new")
    public String startNewGame(@RequestBody RoomDto roomDto) {
        Long gameId = chessGameService.createGame(roomDto.getTitle(), roomDto.getPassword());
        return gameId.toString();
    }

    @GetMapping("/board/{gameId}")
    public Map<String, String> startGame(@PathVariable Long gameId) {
        WebBoardDto board = chessGameService.continueGame(gameId);
        return board.getWebBoard();
    }

    @GetMapping(value = "/turn/{gameId}")
    public String turn(@PathVariable Long gameId) {
        return chessGameService.getTurn(gameId);
    }

    @PostMapping("/move/{gameId}")
    public Map<String, String> move(@PathVariable Long gameId, @RequestBody MoveDto moveCommand) {
        WebBoardDto board = chessMoveService.move(gameId, moveCommand);
        return board.getWebBoard();
    }

    @GetMapping("/king/dead/{gameId}")
    public boolean kingDead(@PathVariable Long gameId) {
        return chessBoardService.isKingDead(gameId);
    }

    @GetMapping("/status/{gameId}")
    public GameResult status(@PathVariable Long gameId) {
        return chessBoardService.getResult(gameId);
    }

    @PatchMapping("/exit/{gameId}")
    public void exit(@PathVariable Long gameId) {
        chessGameService.exitGame(gameId);
    }

    @PostMapping("/restart/{gameId}")
    public void restartGame(@PathVariable Long gameId) {
        chessGameService.restartGame(gameId);
    }

    @DeleteMapping("/room/{gameId}")
    public void deleteGame(@PathVariable Long gameId, @RequestBody String password) {
        chessGameService.deleteGame(gameId, password);
    }
}
