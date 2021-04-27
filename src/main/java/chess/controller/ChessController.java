package chess.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import chess.domain.chess.Chess;
import chess.domain.chess.ChessDto;
import chess.domain.position.MovePosition;
import chess.service.ChessService;

@Controller
@RequestMapping("/api/chess")
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping
    public ResponseEntity<ChessDto> readChess(@RequestParam long chessId) {
        Chess chess = chessService.findChessById(chessId);
        ChessDto chessDto = new ChessDto(chess);
        return ResponseEntity.ok(chessDto);
    }

    @PatchMapping
    public ResponseEntity<Chess> move(@RequestParam long chessId, MovePosition movePosition) {
        final Chess updatedChess = chessService.move(chessId, movePosition);
        return ResponseEntity.ok(updatedChess);
    }
}
