package chess.controller.api;

import chess.dto.ChessGameDto;
import chess.dto.ChessGamesDto;
import chess.dto.TitleDto;
import chess.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
public class LobbyController {

    private final GameService gameService;

    public LobbyController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/game")
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
