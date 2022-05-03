package chess.controller;

import chess.dto.request.CreateGameRequest;
import chess.dto.request.MoveRequest;
import chess.dto.response.ChessGameResponse;
import chess.dto.response.ChessGamesResponse;
import chess.dto.response.StatusResponse;
import chess.service.ChessService;
import java.net.URI;
import java.net.URISyntaxException;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
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
        return chessService.loadAllGames();
    }

    @GetMapping("/{gameId}")
    public ChessGameResponse loadGame(@PathVariable @DecimalMin(value = "0") Long gameId) {
        return chessService.loadGame(gameId);
    }

    @PostMapping
    public ResponseEntity<Void> createGame(@RequestBody @Valid CreateGameRequest createGameRequest)
            throws URISyntaxException {
        Long gameId = chessService.createGame(createGameRequest);
        return ResponseEntity.created(new URI("/game/" + gameId)).build();
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
    public ChessGameResponse movePiece(@PathVariable Long gameId, @RequestBody @Valid MoveRequest moveRequest) {
        return chessService.move(gameId, moveRequest);
    }

    @GetMapping("/{gameId}/status")
    public StatusResponse status(@PathVariable Long gameId) {
        return chessService.status(gameId);
    }

    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> endGame(@PathVariable Long gameId, @RequestHeader HttpHeaders headers) {
        String password = headers.getFirst("Password");
        chessService.deleteGame(gameId, password);
        return ResponseEntity.noContent().build();
    }
}

