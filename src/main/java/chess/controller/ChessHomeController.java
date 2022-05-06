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

    @PostMapping("/games")
    public ResponseEntity<Void> startNewGame(@RequestBody RoomRequest roomRequest) {
        Long gameId = chessService.start(roomRequest);
        return ResponseEntity.created(URI.create("/games/" + gameId)).build();
    }

    @GetMapping("/games")
    public ResponseEntity<GameInfosResponse> getAllGames() {
        return ResponseEntity.ok(chessService.getAllGames());
    }
}
