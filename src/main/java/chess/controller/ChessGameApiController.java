package chess.controller;

import chess.domain.game.dto.BoardResponse;
import chess.domain.game.dto.MoveRequest;
import chess.domain.gameRoom.ChessGame;
import chess.service.ChessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChessGameApiController {

    private final ChessService chessService;

    public ChessGameApiController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/chess/game/{id}/board")
    public ResponseEntity<BoardResponse> showBoard(@PathVariable String id) {
        ChessGame chessGame = chessService.getChessGame(id);
        return new ResponseEntity<>(
                new BoardResponse(chessGame),
                HttpStatus.OK
        );
    }

    @PostMapping("/chess/game/{id}/move")
    public ResponseEntity<BoardResponse> movePiece(@PathVariable String id, MoveRequest moveRequest) {
        ChessGame chessGame = chessService.movePiece(id, moveRequest);
        return new ResponseEntity<>(
                new BoardResponse(chessGame),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/chess/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable String id, @RequestParam String password) {
        chessService.deleteGameByIdAndPassword(id, password);
        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT
        );
    }
}
