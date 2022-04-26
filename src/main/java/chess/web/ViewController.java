package chess.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import chess.service.RoomService;

@Controller
public class ViewController {

    private final RoomService roomService;

    public ViewController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/")
    public String login() {
        return "login.html";
    }

    @GetMapping("/rooms/{roomId}")
    public String board(@PathVariable int roomId) {
        roomService.validateId(roomId);
        return "/board.html";
    }
}
