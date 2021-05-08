package chess.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/rooms")
@Controller
public class RoomController {

    @GetMapping
    public String showRooms() {
        return "/html/game-list.html";
    }

}
