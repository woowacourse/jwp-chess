package chess.controller;

import chess.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChessWebController {

    private final RoomService roomService;

    public ChessWebController(final RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/room/{roomName}")
    public String room(@PathVariable final String roomName) {
        return "room";
    }
}
