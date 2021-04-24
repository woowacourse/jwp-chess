package chess.web;

import chess.service.RoomService;
import chess.web.dto.RoomDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/{roomName}")
    public RoomDto createNewRoom(@PathVariable String roomName) {
        return roomService.createNewRoom(roomName);
    }

    @GetMapping
    public List<RoomDto> rooms() {
        return roomService.getAllRooms();
    }

}
