package chess.controller.spring;

import chess.dto.*;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/game")
public class ChessController {
    private final ChessService service;

    public ChessController(ChessService service) {
        this.service = service;
    }

    @GetMapping()
    public String index() {
        return "index.html";
    }

    @GetMapping("/new")
    public ResponseEntity<CommonDto<NewGameDto>> newGame() {
        return ResponseEntity.ok().body(service.saveNewGame());
    }

    @PostMapping("/{gameId:[\\d]+}/move")
    public ResponseEntity<CommonDto<RunningGameDto>> move(@RequestBody MoveRequest moveRequest) {
        return ResponseEntity.ok().body(service.move(moveRequest.getGameId(), moveRequest.getFrom(), moveRequest.getTo()));
    }

    @GetMapping("/{gameId:[\\d]+}/load")
    public ResponseEntity<CommonDto<RunningGameDto>> loadGame(@PathVariable int gameId) {
        return ResponseEntity.ok().body(service.loadGame(gameId));
    }
}