package chess.controller.spring;

import chess.dto.CommonDto;
import chess.dto.MovePositionDto;
import chess.dto.NewGameDto;
import chess.dto.RunningGameDto;
import chess.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/new")
    public ResponseEntity<CommonDto<NewGameDto>> newGame() {
        return ResponseEntity.ok().body(gameService.saveNewGame());
    }

    @PostMapping("/{gameId:[\\d]+}/move")
    public ResponseEntity<CommonDto<RunningGameDto>> move(@RequestBody MovePositionDto movePositionDto) {
        return ResponseEntity.ok()
                .body(gameService.move(
                        movePositionDto.getGameId(),
                        movePositionDto.getFrom(),
                        movePositionDto.getTo()));
    }

    @GetMapping("/{gameId:[\\d]+}/load")
    public ResponseEntity<CommonDto<RunningGameDto>> load(@PathVariable int gameId) {
        return ResponseEntity.ok().body(gameService.loadGame(gameId));
    }
}