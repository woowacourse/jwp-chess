package chess.controller;

import chess.controller.dto.request.CreateGameRequest;
import chess.controller.dto.request.MoveRequest;
import chess.controller.dto.response.ChessGameResponse;
import chess.controller.dto.response.ChessGamesResponse;
import chess.controller.dto.response.EndResponse;
import chess.controller.dto.response.StatusResponse;
import chess.service.ChessService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/games")
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping
    public ChessGamesResponse loadGameList() {
        return chessService.findAllGameIds();
    }

    @GetMapping("/{gameId}")
    public ChessGameResponse loadGame(@PathVariable long gameId) {
        return chessService.loadGame(gameId);
    }

    @PostMapping
    public ResponseEntity<ChessGameResponse> createGame(@RequestBody CreateGameRequest createGameRequest)
            throws URISyntaxException {
        long gameId = Math.abs(new Random().nextInt());
        return ResponseEntity.created(new URI("/game/" + gameId))
                .body(chessService.createGame(gameId, createGameRequest));
    }

    @PatchMapping("/{gameId}")
    public ChessGameResponse startGame(@PathVariable long gameId) {
        return chessService.startGame(gameId);
    }

    @PutMapping("/{gameId}")
    public ChessGameResponse resetGame(@PathVariable long gameId) {
        return chessService.resetGame(gameId);
    }

    @PatchMapping("/{gameId}/pieces")
    public ChessGameResponse movePiece(@PathVariable long gameId, @RequestBody MoveRequest moveRequest) {
        return chessService.move(gameId, moveRequest);
    }

    @GetMapping("/{gameId}/status")
    public StatusResponse status(@PathVariable long gameId) {
        return chessService.status(gameId);
    }

    @DeleteMapping("/{gameId}")
    public EndResponse endGame(@PathVariable long gameId) {
        return chessService.endGame(gameId);
    }
}

