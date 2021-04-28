package chess.controller.api;

import chess.dto.ChessGameDto;
import chess.dto.ChessGamesDto;
import chess.dto.TitleDto;
import chess.service.LobbyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
public class LobbyController {

    private final LobbyService lobbyService;

    public LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @PostMapping("/game")
    public ResponseEntity<String> newGame(@RequestBody @Valid TitleDto titleDto) {
        lobbyService.verifyDuplicateTitleInGames(titleDto.getTitle());
        return ResponseEntity.ok(lobbyService.newGame(titleDto.getTitle()));
    }

    @GetMapping("/games")
    public ResponseEntity<ChessGamesDto> findAllRooms() {
        return ResponseEntity.ok(new ChessGamesDto(
                lobbyService.findAllGames()
                        .stream()
                        .map(ChessGameDto::new)
                        .collect(Collectors.toList())
        ));
    }
}
