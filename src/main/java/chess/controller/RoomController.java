package chess.controller;

import chess.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;

    public RoomController(final RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/list")
    public ModelAndView loadRoomList() {
        final ModelAndView view = new ModelAndView("mainPage");
        view.addObject("list", roomService.loadList());
        return view;
    }
}