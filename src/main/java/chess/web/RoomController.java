package chess.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import chess.service.RoomService;
import chess.domain.Room;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public String createRoom(@RequestParam String name, @RequestParam String password) {
        Room room = roomService.create(new Room(name, password));
        return "redirect:/rooms/" + room.getId();
    }

    @GetMapping("/{roomId}")
    public String board(@PathVariable int roomId) {
        roomService.getRoom(roomId);
        return "/board.html";
    }
}
