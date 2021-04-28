package chess.controller;


import chess.dto.game.GameResponseDto;
import chess.dto.game.move.MoveRequestDto;
import chess.dto.game.move.MoveResponseDto;
import chess.service.GameService;
import java.util.Collections;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/games")
@RestController
public class GameApiController {

    private final GameService gameService;

    public GameApiController(final GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameResponseDto> bringGameData(@PathVariable long gameId) {
        return ResponseEntity.ok().body(gameService.retrieveGameData(gameId));
    }

    @GetMapping("/{gameId}/move/check")
    public ResponseEntity<Map<String, Boolean>> checkMovement(@PathVariable long gameId,
        @ModelAttribute MoveRequestDto moveRequestDto) {

        final Map<String, Boolean> responseData = Collections
            .singletonMap("isMovable", gameService.checkMovement(gameId, moveRequestDto));
        return ResponseEntity.ok().body(responseData);
    }

    @PutMapping("/{gameId}/move")
    public ResponseEntity<MoveResponseDto> move(@PathVariable long gameId,
        @RequestBody MoveRequestDto moveRequestDto) {

        return ResponseEntity.ok().body(gameService.move(gameId, moveRequestDto));
    }

}
