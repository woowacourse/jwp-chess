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

    @GetMapping("/rooms/{roomName}")
    public String room(@PathVariable("roomName") final String roomName) {
        final boolean roomExist = roomService.isExistRoom(roomName);
        if (!roomExist) {
            return "redirect:/";
        }

        return "board";
    }
}
