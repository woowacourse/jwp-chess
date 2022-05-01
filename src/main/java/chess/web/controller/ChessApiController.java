package chess.web.controller;

import chess.web.dto.board.GameOverDto;
import chess.web.dto.board.MovePositionsDto;
import chess.web.dto.game.PasswordDto;
import chess.web.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessApiController {

    private final ChessService chessService;

    public ChessApiController(ChessService chessService) {
        this.chessService = chessService;
    }

    @DeleteMapping
    public ResponseEntity<?> deleteGame(@RequestBody PasswordDto passwordDto) {
        chessService.deleteGame(passwordDto);
        return ResponseEntity.ok("");
    }

    @PatchMapping("/game/{gameId}/move")
    public ResponseEntity<GameOverDto> move(@PathVariable int gameId,
                                            @RequestBody MovePositionsDto movePositionsDto) {
        return ResponseEntity.ok(chessService.getIsGameOver(gameId, movePositionsDto));
    }
}
