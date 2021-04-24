package chess.controller;

import chess.service.SpringChessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public final class SpringChessController {

    private final SpringChessService springChessService;

    public SpringChessController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    @GetMapping("/")
    public String lobby(Model model) {
        model.addAttribute("rooms", springChessService.openedRooms());
        return "lobby";
    }

    @GetMapping("/room/{id}")
    public String joinRoom(@PathVariable String id, Model model) {
        model.addAttribute("board", springChessService.latestBoard(id));
        model.addAttribute("roomId", id);
        model.addAttribute("userInfo", springChessService.usersInRoom(id));
        return "index";
    }
}
