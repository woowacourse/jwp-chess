package chess.controller;

import chess.dto.GameRequestDto;
import chess.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

@Controller
@RequestMapping("/games")
public class GameController {

    final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity createGame(@RequestBody GameRequestDto gameRequestDto) {
        final long id = gameService.add(gameRequestDto);

        return ResponseEntity.created(URI.create("/games/" + id)).build();
    }
}
