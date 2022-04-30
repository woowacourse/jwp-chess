package chess.controller;

import chess.service.RoomService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("rooms", roomService.findAllRooms());
        return modelAndView;
    }

    @GetMapping("/room/create")
    public String createRoomPage() {
        return "createRoom";
    }

    @PostMapping("/room")
    public String createRoom(@RequestParam String name, @RequestParam String password) {
        roomService.create(name, password);
        return "redirect:/";
    }
}
