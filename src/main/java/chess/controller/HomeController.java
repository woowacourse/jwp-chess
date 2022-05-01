package chess.controller;

import chess.dto.GameCreationDto;
import chess.dto.GameDto;
import chess.serviece.ChessGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HomeController {

    private final ChessGameService chessGameService;

    public HomeController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping
    public String init() {
        return "home";
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<GameDto>> getGames() {
        return ResponseEntity.ok().body(chessGameService.getAllGames());
    }

    @PostMapping(value = "/room")
    public ResponseEntity<Long> createGame(@RequestBody GameCreationDto gameCreationDto) {
        return ResponseEntity.ok().body(chessGameService.addGame(gameCreationDto));
    }

    @DeleteMapping(value = "/room/{id}")
    public ResponseEntity<Long> deleteGame(@PathVariable Long id, @RequestBody GameDto gameDto) {
        chessGameService.removeGame(id, gameDto);
        return ResponseEntity.noContent().build();
    }
}
