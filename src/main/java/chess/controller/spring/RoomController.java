package chess.controller.spring;

import chess.dto.CommonDto;
import chess.dto.GameListDto;
import chess.service.ChessService;
import chess.dto.RoomDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/room")
public class RoomController {
    private final ChessService service;

    public RoomController(ChessService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public ResponseEntity<CommonDto<GameListDto>> loadGames() {
        return ResponseEntity.ok().body(service.loadGameList());
    }

    @PostMapping("/new")
    public ResponseEntity<CommonDto<RoomDto>> saveRoomName(@RequestBody RoomDto roomDto) {
        return ResponseEntity.ok().body(service.saveRoom(roomDto));
    }

    @GetMapping("/{gameId:[\\d]+}/load")
    public ResponseEntity<String> findRoomName(@PathVariable int gameId) {
        return ResponseEntity.ok().body(service.loadRoomName(gameId));
    }
}
