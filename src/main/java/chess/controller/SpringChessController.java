package chess.controller;

import chess.dto.ChessDto;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringChessController {

    private final ChessService chessService;

    public SpringChessController(final ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/start")
    public ResponseEntity<ChessDto> start() {
        return ResponseEntity.ok(chessService.initializeGame());
    }
}
