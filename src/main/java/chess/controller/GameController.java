package chess.controller;

import chess.dto.GameCountResponseDto;
import chess.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/games")
@RestController
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/roomcnt")
    public ResponseEntity<GameCountResponseDto> roomCount() {
        return ResponseEntity.ok().body(gameService.gameCount());
    }

}
