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
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
    public ChessGameResponse loadGame(@PathVariable Long gameId) {
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
    public ChessGameResponse startGame(@PathVariable Long gameId) {
        return chessService.startGame(gameId);
    }

    @PutMapping("/{gameId}")
    public ChessGameResponse resetGame(@PathVariable Long gameId) {
        return chessService.resetGame(gameId);
    }

    @PatchMapping("/{gameId}/pieces")
    public ChessGameResponse movePiece(@PathVariable Long gameId, @RequestBody MoveRequest moveRequest) {
        return chessService.move(gameId, moveRequest);
    }

    @GetMapping("/{gameId}/status")
    public StatusResponse status(@PathVariable Long gameId) {
        return chessService.status(gameId);
    }

    @DeleteMapping("/{gameId}")
    public EndResponse endGame(@PathVariable Long gameId, @RequestHeader HttpHeaders headers) {
        String password = headers.getFirst("Authorization");
        return chessService.endGame(gameId, password);
    }
}

