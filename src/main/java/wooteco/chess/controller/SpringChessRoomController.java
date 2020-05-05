package wooteco.chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wooteco.chess.service.RoomService;

@Controller
public class SpringChessRoomController {
    private final RoomService roomService;

    public SpringChessRoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("roomInformation", roomService.loadRoomInformation());
        return "index";
    }

    @PostMapping("/newroom")
    public String newRoom(@RequestParam String title) {
        if (title.trim().isEmpty()) {
            return "redirect:/";
        }
        Long roomId = roomService.create(title);
        return "redirect:/rooms/" + roomId;
    }

}
