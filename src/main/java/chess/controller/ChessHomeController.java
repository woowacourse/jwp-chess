package chess.controller;

import chess.model.dto.GameInfosResponse;
import chess.model.dto.RoomRequest;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class ChessHomeController {

    private final ChessService chessService;

    public ChessHomeController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("/new")
    public ResponseEntity<Void> startNewGame(@RequestBody RoomRequest roomRequest) {
        chessService.start(roomRequest);
        return ResponseEntity.created(URI.create("/")).build();
    }

    @GetMapping("/games")
    public ResponseEntity<GameInfosResponse> getAllGames() {
        return ResponseEntity.ok(chessService.getAllGames());
    }
}
