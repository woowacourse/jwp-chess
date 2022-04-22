package chess.controller;

import chess.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChessWebController {

    private static final String INDEX = "index";

    private final RoomService roomService;

    public ChessWebController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/")
    public String index() {
        return INDEX;
    }

    @GetMapping("/rooms/{roomName}")
    public String room(@PathVariable("roomName") String roomName) {
        final boolean roomExist = roomService.isExistRoom(roomName);
        if (!roomExist) {
            return "redirect:/";
        }

        return "board";
    }
}
