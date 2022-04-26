package chess.controller;

import chess.dto.GameRoomDto;
import chess.service.ChessService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/games")
public class GamesController {

    private final ChessService chessService;

    public GamesController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping
    public ResponseEntity<List<GameRoomDto>> findOnPlay() {
        return new ResponseEntity<>(chessService.findGamesOnPlay(), HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameRoomDto> findById(@PathVariable("id") int id) {
        return new ResponseEntity<>(chessService.findGameById(id), HttpStatus.FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable("id") int id) {
        return new ResponseEntity<>(chessService.deleteGameById(id) == id, HttpStatus.GONE);
    }
}
