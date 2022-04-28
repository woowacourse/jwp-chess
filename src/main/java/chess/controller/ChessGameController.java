package chess.controller;

import chess.dto.*;
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
    public ResponseEntity<BoardDto> loadBoard(@PathVariable Long id) {
        BoardDto initialBoard = chessService.getBoard(id);
        return ResponseEntity.ok().body(initialBoard);
    }

    @PostMapping(value = "/move/{id}")
    public ResponseEntity<GameStateDto> move(@PathVariable Long id, @RequestBody MoveDto moveDto) {
        return ResponseEntity.ok().body(chessService.move(id, moveDto));
    }

    @GetMapping("/status")
    public ResponseEntity<ScoreDto> getStatus() {
        return ResponseEntity.ok().body(chessService.getStatus());
    }

    @PostMapping("/reset/{id}")
    public ResponseEntity<BoardDto> resetGame(@PathVariable Long id) {
        chessService.resetBoard(chessService.findRoom(id), id);
        return ResponseEntity.ok().body(chessService.getBoard(id));
    }

    @GetMapping("/loadGames")
    public ResponseEntity<GamesDto> loadGames() {
        return ResponseEntity.ok().body(new GamesDto(chessService.getGameList()));
    }

    @PostMapping("/end/{id}")
    public ResponseEntity<GameStateDto> endGame(@PathVariable Long id) {
        chessService.updateEndStatus(id);
        return ResponseEntity.ok().body(chessService.findWinner());
    }
}
