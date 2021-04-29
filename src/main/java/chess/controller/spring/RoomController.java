package chess.controller.spring;

import chess.dto.CommonDto;
import chess.dto.RoomDto;
import chess.dto.RoomListDto;
import chess.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/room")
public class RoomController {
    private final RoomService service;

    public RoomController(RoomService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public ResponseEntity<CommonDto<RoomListDto>> roomList() {
        return ResponseEntity.ok(service.list());
    }

    @PostMapping("/new")
    public ResponseEntity<CommonDto<RoomDto>> saveRoom(@RequestBody RoomDto roomDto) {
        return ResponseEntity.ok(service.save(roomDto));
    }

    @GetMapping("/{gameId}/load")
    public ResponseEntity<String> findRoomName(@PathVariable int gameId) {
        return ResponseEntity.ok(service.loadRoomName(gameId));
    }
}
