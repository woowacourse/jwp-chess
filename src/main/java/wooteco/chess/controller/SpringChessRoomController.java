package wooteco.chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import wooteco.chess.service.RoomService;

@Controller
public class SpringChessRoomController {
    private final RoomService roomService;

    public SpringChessRoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("roomNumbers", roomService.loadRoomNumbers());
        return "index";
    }

    @PostMapping("/newroom")
    public String newRoom() {
        Long roomId = roomService.create();
        return "redirect:/rooms/" + roomId;
    }

}
