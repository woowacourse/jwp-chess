package chess.controller.spring;

import chess.dto.CommonDto;
import chess.dto.RoomDto;
import chess.dto.RoomListDto;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/room")
public class RoomController {
    private final ChessService service;

    public RoomController(ChessService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public ResponseEntity<CommonDto<RoomListDto>> loadGames() {
        return ResponseEntity.ok().body(service.loadRoomList());
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
