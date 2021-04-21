package chess.controller;

import chess.service.game.ChessGameService;
import dto.ChessGameDto;
import dto.MoveDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ChessGameController {
    private final ChessGameService chessGameService;

    public ChessGameController(final ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @PutMapping("/game/{id}")
    public ResponseEntity<ChessGameDto> movePiece(@PathVariable("id") Long gameId, @RequestBody MoveDto moveDto) {
        return ResponseEntity.ok().body(chessGameService.move(gameId, moveDto));
    }
}
