package chess.controller;

import chess.dto.*;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/game")
public class SpringWebChessController {
    private final ChessService service;

    public SpringWebChessController(ChessService service) {
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

    @PostMapping("/move")
    public ResponseEntity<CommonDto<RunningGameDto>> move(@RequestBody MoveRequest moveRequest) {
        return ResponseEntity.ok().body(service.move(moveRequest.getGameId(), moveRequest.getFrom(), moveRequest.getTo()));
    }

    @PostMapping("/room/name")
    public ResponseEntity<CommonDto<RoomDto>> saveRoomName(@RequestBody String roomName) {
        return ResponseEntity.ok().body(service.saveRoom(roomName));
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