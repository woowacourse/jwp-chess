package chess.controller;

import chess.service.RoomService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessApiController {

    private final RoomService roomService;

    public ChessApiController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/rooms/{roomName}")
    public void createRoom(@PathVariable("roomName") String roomName) {
        roomService.createRoom(roomName);
    }
}
