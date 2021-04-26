package chess.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/room")
@Controller
public class RoomController {

    @GetMapping("{roomId:[\\d]+}")
    public String enterRoom(@PathVariable long roomId, Model model) {
        model.addAttribute("roomId", roomId);
        return "chess";
    }
}
