package chess.controller;

import chess.dto.response.*;
import chess.service.ChessService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.SQLException;

@RestController
public class ChessGameController {

    private final ChessService chessService;

    public ChessGameController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/games/{id}")
    public ResponseEntity<BoardResponse> loadBoard(@PathVariable Long id) {
        BoardResponse initialBoard = chessService.getBoard(id);
        return ResponseEntity.ok().body(initialBoard);
    }

    @PutMapping( "/move/{id}")
    public ResponseEntity<GameStateResponse> move(@PathVariable Long id,
                                                  @RequestBody MoveResponse moveResponse) throws SQLException {
        chessService.validateGameId(id);
        return ResponseEntity.created(URI.create("/move/" + id)).body(chessService.move(id, moveResponse));
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<ScoreResponse> getStatus(@PathVariable Long id) throws SQLException {
        chessService.validateGameId(id);
        return ResponseEntity.ok().body(chessService.getStatus(id));
    }

    @PostMapping("/reset/{id}")
    public ResponseEntity<BoardResponse> resetGame(@PathVariable Long id) throws SQLException {
        chessService.validateGameId(id);
        chessService.resetBoard(id);
        return ResponseEntity.created(URI.create("/reset/" + id)).body(chessService.getBoard(id));
    }

    @GetMapping("/games")
    public ResponseEntity<GamesResponse> loadGames() {
        return ResponseEntity.ok().body(new GamesResponse(chessService.getGames()));
    }

    @PutMapping("/end/{id}")
    public ResponseEntity<GameStateResponse> endGameState(@PathVariable Long id) throws SQLException {
        chessService.validateGameId(id);
        chessService.updateStateEnd(id);
        return ResponseEntity.ok().body(chessService.findWinner(id));
    }
}
