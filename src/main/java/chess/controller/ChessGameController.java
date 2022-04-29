package chess.controller;

import chess.dto.response.*;
import chess.service.ChessService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChessGameController {

    private final ChessService chessService;

    public ChessGameController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/loadBoard/{id}")
    public ResponseEntity<BoardResponse> loadBoard(@PathVariable Long id) {
        BoardResponse initialBoard = chessService.getBoard(id);
        return ResponseEntity.ok().body(initialBoard);
    }

    @PostMapping( "/move/{id}")
    public ResponseEntity<GameStateResponse> move(@PathVariable Long id, @RequestBody MoveResponse moveResponse) {
        return ResponseEntity.ok().body(chessService.move(id, moveResponse));
    }

    @GetMapping("/status")
    public ResponseEntity<ScoreResponse> getStatus() {
        return ResponseEntity.ok().body(chessService.getStatus());
    }

    @PostMapping("/reset/{id}")
    public ResponseEntity<BoardResponse> resetGame(@PathVariable Long id) {
        chessService.resetBoard(id);
        return ResponseEntity.ok().body(chessService.getBoard(id));
    }

    @GetMapping("/loadGames")
    public ResponseEntity<GamesResponse> loadGames() {
        return ResponseEntity.ok().body(new GamesResponse(chessService.getGameList()));
    }

    @PostMapping("/end/{id}")
    public ResponseEntity<GameStateResponse> endGame(@PathVariable Long id) {
        chessService.updateEndStatus(id);
        return ResponseEntity.ok().body(chessService.findWinner());
    }
}
