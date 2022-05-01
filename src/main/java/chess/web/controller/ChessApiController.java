package chess.web.controller;

import chess.web.dto.board.MovePositionsDto;
import chess.web.dto.board.MoveResultDto;
import chess.web.dto.game.PasswordDto;
import chess.web.service.ChessService;
import org.springframework.http.HttpStatus;
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

    @DeleteMapping("/game/{gameId}")
    public ResponseEntity<Void> deleteGame(@RequestBody PasswordDto passwordDto) {
        chessService.deleteGame(passwordDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/game/{gameId}/move")
    public ResponseEntity<MoveResultDto> move(@PathVariable int gameId,
                                              @RequestBody MovePositionsDto movePositionsDto) {
        return ResponseEntity.ok(chessService.getMoveResult(gameId, movePositionsDto));
    }
}
