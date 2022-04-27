package chess.controller;

import chess.model.GameResult;
import chess.model.dto.GameInfosDto;
import chess.model.dto.MoveDto;
import chess.model.dto.RoomDto;
import chess.model.dto.WebBoardDto;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ChessApiController {

    private final ChessService chessService;

    public ChessApiController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("/new")
    public Map<String, String> startNewGame(@RequestBody RoomDto roomDto) {
        WebBoardDto board = chessService.start(roomDto);
        return board.getWebBoard();
        // 생각해볼 부분: ResponseEntity의 사용
    }

    @GetMapping("/games")
    public ResponseEntity<GameInfosDto> getAllGames() {
        return ResponseEntity.ok(chessService.getAllGames());
    }

    @GetMapping("/game/{id}")
    public Map<String, String> getGame(@PathVariable Long id) {
        WebBoardDto board = chessService.getBoardByGameId(id);
        return board.getWebBoard();
    }

    @DeleteMapping("/game/{id}")
    public void deleteGame(@PathVariable Long id, @RequestBody String confirmPwd) {
        chessService.deleteByGameId(confirmPwd, id);
    }

    @PostMapping("/game/{id}/move")
    public Map<String, String> move(@PathVariable Long id, @RequestBody MoveDto moveCommand) {
        WebBoardDto board = chessService.move(moveCommand, id);
        return board.getWebBoard();
    }

    @GetMapping("/game/{id}/turn")
    public String turn(@PathVariable Long id) {
        return chessService.getTurn(id);
    }

    @GetMapping("/game/{id}/dead")
    public boolean isKingDead(@PathVariable Long id) {
        return chessService.isKingDead(id);
    }

    @GetMapping("/game/{id}/status")
    public GameResult status(@PathVariable Long id) {
        return chessService.getResult(id);
    }
}
