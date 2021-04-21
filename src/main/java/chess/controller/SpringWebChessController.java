package chess.controller;

import chess.dto.*;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SpringWebChessController {
    private final ChessService service;

    public SpringWebChessController(ChessService service) {
        this.service = service;
    }

    @GetMapping("/newgame")
    public ResponseEntity<CommonDto<NewGameDto>> newGame() {
        return ResponseEntity.ok().body(service.saveNewGame());
    }

    @PostMapping("/move")
    public ResponseEntity<CommonDto<RunningGameDto>> move(@RequestBody MoveRequest moveRequest) {
        return ResponseEntity.ok().body(service.move(moveRequest.getGameId(), moveRequest.getFrom(), moveRequest.getTo()));
    }

    @GetMapping("/load/games")
    public ResponseEntity<CommonDto<GameListDto>> loadGames() {
        return ResponseEntity.ok().body(service.loadGameList());
    }

    @GetMapping("/load/{gameId}")
    public ResponseEntity<CommonDto<RunningGameDto>> loadGame(@PathVariable int gameId) {
        return ResponseEntity.ok().body(service.loadGame(gameId));
    }
}