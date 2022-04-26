package chess.controller;

import chess.dto.GameRoomDataDto;
import chess.service.GameRoomService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GameRoomController {

    private final GameRoomService gameRoomService;

    public GameRoomController(GameRoomService gameRoomService) {
        this.gameRoomService = gameRoomService;
    }

    @GetMapping("/")
    public ModelAndView getRooms() {
        List<GameRoomDataDto> rooms = gameRoomService.getAllGameRooms();

        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("rooms", rooms);

        return modelAndView;
    }

}
