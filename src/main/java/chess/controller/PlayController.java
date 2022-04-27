package chess.controller;

import chess.dto.MoveDto;
import chess.service.ChessGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class PlayController {
    private final ChessGameService chessGameService;

    public PlayController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @PutMapping("/{gameId}/move")
    public ResponseEntity<String> move(@PathVariable String gameId, @RequestBody MoveDto moveDto) {
        chessGameService.move(gameId, moveDto);

        if (!chessGameService.calculateGameResult(gameId).getWinner().equals("없음")) {
            chessGameService.changeToEnd(gameId);
        }
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exception(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
