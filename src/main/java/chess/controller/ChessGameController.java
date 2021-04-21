package chess.controller;

import chess.domain.Room;
import chess.service.ChessGameService;
import chess.service.ChessRoomService;
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
