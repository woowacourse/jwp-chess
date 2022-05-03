package chess.web.controller;

import chess.web.dto.board.MoveResponseDto;
import chess.web.dto.board.MoveRequestDto;
import chess.web.dto.game.GameRequestDto;
import chess.web.service.ChessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessApiController {

    private final ChessService chessService;

    public ChessApiController(ChessService chessService) {
        this.chessService = chessService;
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGame(@RequestBody GameRequestDto gameRequestDto) {
        chessService.deleteGame(gameRequestDto);
    }

    @PatchMapping("/game/{gameId}/move")
    public ResponseEntity<MoveResponseDto> move(@PathVariable int gameId,
                                                @RequestBody MoveRequestDto moveRequestDto) {
        return ResponseEntity.ok(chessService.getMoveResult(gameId, moveRequestDto));
    }
}
