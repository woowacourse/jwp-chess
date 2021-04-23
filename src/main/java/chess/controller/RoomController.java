package chess.controller;

import chess.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;

    public RoomController(final RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/list")
    public String loadRoomList(final Model model) {
        model.addAttribute("list", roomService.loadList());
        return "mainPage";
    }
}