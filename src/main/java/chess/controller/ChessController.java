package chess.controller;

import chess.controller.dto.request.MoveRequest;
import chess.controller.dto.response.ChessGameResponse;
import chess.controller.dto.response.ErrorResponse;
import chess.controller.dto.response.StatusResponse;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping("/start/{gameId}")
    public ChessGameResponse startGame(@PathVariable long gameId) {
        return chessService.startGame(gameId);
    }

    @GetMapping("/restart/{gameId}")
    public ChessGameResponse restartGame(@PathVariable long gameId) {
        return chessService.restartGame(gameId);
    }

    @PostMapping("/move/{gameId}")
    public ChessGameResponse movePiece(@PathVariable long gameId, @RequestBody MoveRequest moveRequest) {
        return chessService.move(gameId, moveRequest);
    }

    @GetMapping("/status/{gameId}")
    public StatusResponse status(@PathVariable long gameId) {
        return chessService.status(gameId);
    }

    @GetMapping("/end/{gameId}")
    public ChessGameResponse endGame(@PathVariable long gameId) {
        return chessService.end(gameId);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleError(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }
}

