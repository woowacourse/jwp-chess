package chess.controller;

import chess.model.dto.GameInfosDto;
import chess.model.dto.RoomDto;
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
    public ResponseEntity startNewGame(@RequestBody RoomDto roomDto) {
        chessService.start(roomDto);
        return ResponseEntity.created(URI.create("/")).build();
    }

    @GetMapping("/games")
    public ResponseEntity<GameInfosDto> getAllGames() {
        return ResponseEntity.ok(chessService.getAllGames());
    }
}
