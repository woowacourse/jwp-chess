package chess.controller;

import chess.dto.RoomDto;
import chess.service.SpringChessService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;
import java.util.Optional;

@Controller
public class SpringChessController {
    private static final int ROOM_NOT_EXIST = -1;
    private static final int ROOM_ALREADY_EXIST = -2;

    private final SpringChessService springChessService;

    public SpringChessController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    @GetMapping("/")
    public String mainPage() {
        return "main";
    }

    @GetMapping("/chess/start")
    public ModelAndView startGame(@RequestParam("room") String name) {
        Long id = springChessService.createRoom(new RoomDto(name, null));
        final ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("roomId", id);

        return modelAndView;
    }

    @GetMapping("/chess/enter")
    public ModelAndView enterGame(@RequestParam("id") String id) {
        Optional<String> roomId = springChessService.findRoomById(id);

        if (Objects.isNull(roomId)) {
            final ModelAndView modelAndView = new ModelAndView("main");
            modelAndView.addObject("interact", ROOM_ALREADY_EXIST);
            return modelAndView;
        }

        final ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("roomId", roomId.get());

        return modelAndView;
    }

    @GetMapping("/chess/clear/{id}")
    public String clear(@PathVariable String id) {
        springChessService.deleteRoom(id);
        return "redirect:/";
    }
}
