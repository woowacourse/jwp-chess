package chess.controller;

import chess.model.GameResult;
import chess.model.dto.MoveDto;
import chess.model.dto.WebBoardDto;
import chess.service.ChessService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/game")
public class ChessGameController {

    private final ChessService chessService;

    public ChessGameController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/{id}")
    public Map<String, String> getGame(@PathVariable Long id) {
        WebBoardDto board = chessService.getBoardByGameId(id);
        return board.getWebBoard();
    }

    @DeleteMapping("/{id}")
    public void deleteGame(@PathVariable Long id, @RequestBody String confirmPwd) {
        chessService.deleteByGameId(confirmPwd, id);
    }

    @PostMapping("/{id}/move")
    public Map<String, String> move(@PathVariable Long id, @RequestBody MoveDto moveCommand) {
        WebBoardDto board = chessService.move(moveCommand, id);
        return board.getWebBoard();
    }

    @GetMapping("/{id}/turn")
    public String turn(@PathVariable Long id) {
        return chessService.getTurn(id);
    }

    @GetMapping("/{id}/dead")
    public boolean isKingDead(@PathVariable Long id) {
        return chessService.isKingDead(id);
    }

    @GetMapping("/{id}/status")
    public GameResult status(@PathVariable Long id) {
        return chessService.getResult(id);
    }
}
