package chess.controller;

import chess.dto.FinishDTO;
import chess.dto.MoveDTO;
import chess.dto.ScoreDTO;
import chess.dto.TurnDTO;
import chess.service.GameService;
import chess.utils.Serializer;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/chessboard/{gameId}")
    public ResponseEntity loadGame(@PathVariable String gameId) {
        return ResponseEntity.ok(Serializer.deserializeGameAsDTO(gameService.loadGame(gameId)));
    }

    @GetMapping("/turn/{gameId}")
    public ResponseEntity turn(@PathVariable String gameId) {
        return ResponseEntity.ok(new TurnDTO(gameService.turn(gameId)));
    }

    @PutMapping(path = "/move/{gameId}")
    public ResponseEntity move(@PathVariable String gameId, @RequestBody MoveDTO moveDTO) {
        try {
            gameService.move(gameId, moveDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/finishById/{gameId}")
    public ResponseEntity isFinished(@PathVariable String gameId) {
        return ResponseEntity.ok(new FinishDTO(gameService.isFinished(gameId)));
    }

    @PostMapping("/finish/{gameId}")
    public ResponseEntity finish(@PathVariable String gameId) {
        gameService.finish(gameId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/scoreById/{gameId}")
    public ResponseEntity score(@PathVariable String gameId) {
        List<Double> scores = gameService.score(gameId);
        return ResponseEntity.ok(new ScoreDTO(scores.get(0), scores.get(1)));
    }

    @PostMapping("/restart/{gameId}")
    public ResponseEntity restart(@PathVariable String gameId) {
        return ResponseEntity.ok(Serializer.deserializeGameAsDTO(gameService.restart(gameId)));
    }
}
