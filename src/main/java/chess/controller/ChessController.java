package chess.controller;

import chess.controller.dto.response.ChessGameResponse;
import chess.service.ChessService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/load/{gameId}")
    public ChessGameResponse loadGame(@PathVariable long gameId) {
        return chessService.createOrLoadGame(gameId);
    }
}

