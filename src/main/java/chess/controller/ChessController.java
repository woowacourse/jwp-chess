package chess.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chess.domain.chess.Chess;
import chess.domain.chess.ChessDto;
import chess.domain.position.MovePosition;
import chess.service.ChessService;

@RestController
@RequestMapping("/api/chess/{chessId}")
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping
    public ResponseEntity<ChessDto> getChess(@PathVariable long chessId) {
        Chess chess = chessService.findChessById(chessId);
        return ResponseEntity.ok(new ChessDto(chess));
    }

    @PatchMapping
    public ResponseEntity<Chess> move(@PathVariable long chessId, MovePosition movePosition) {
        final Chess updatedChess = chessService.move(chessId, movePosition);
        return ResponseEntity.ok(updatedChess);
    }
}
