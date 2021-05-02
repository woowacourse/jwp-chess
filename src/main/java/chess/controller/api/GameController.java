package chess.controller.api;

import chess.dto.ChessGameDto;
import chess.dto.ChessGamesDto;
import chess.dto.MoveDto;
import chess.dto.TitleDto;
import chess.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/games/{id}/game-info")
    public ResponseEntity<ChessGameDto> loadGame(@PathVariable Long id) {
        return ResponseEntity.ok(new ChessGameDto(gameService.loadGame(id)));
    }

    @PutMapping(path = "/games/{id}/move")
    public ResponseEntity<ChessGameDto> move(@PathVariable Long id, @RequestBody MoveDto moveDto) {

        gameService.move(id, moveDto.getSource(), moveDto.getTarget());
        return loadGame(id);
    }

    @PostMapping("/games/{id}/terminate")
    public ResponseEntity<Void> terminateGame(@PathVariable Long id) {
        gameService.terminateGame(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/games/{id}/restart")
    public ResponseEntity<ChessGameDto> restart(@PathVariable Long id) {
        return ResponseEntity.ok(new ChessGameDto(gameService.restart(id)));
    }

    @PostMapping("/games/new-game")
    public ResponseEntity<Long> newGame(@RequestBody @Valid TitleDto titleDto) {
        gameService.verifyDuplicateTitleInGames(titleDto.getTitle());
        return ResponseEntity.ok(gameService.newGame(titleDto.getTitle()));
    }

    @GetMapping("/games")
    public ResponseEntity<ChessGamesDto> findAllRooms() {
        return ResponseEntity.ok(new ChessGamesDto(
                gameService.findAllGames()
                        .stream()
                        .map(ChessGameDto::new)
                        .collect(Collectors.toList())
        ));
    }
}
