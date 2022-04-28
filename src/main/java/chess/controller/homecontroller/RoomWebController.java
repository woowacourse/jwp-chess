package chess.controller.homecontroller;

import chess.dto.RoomDto;
import chess.service.RoomService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RoomWebController {

    private static final String RE_DIRECT = "redirect:/";

    private final RoomService roomService;

    public RoomWebController(final RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping(path = "/")
    public ModelAndView initial() {
        final Map<String, Object> model = new HashMap<>();
        final List<RoomDto> roomNames = roomService.getRoomNames();
        model.put("rooms", roomNames);
        return new ModelAndView("home", model);
    }

    @GetMapping(path = "/makeRoom")
    public String makeRoom(@RequestParam String roomName, @RequestParam String password) {
        roomService.saveNewRoom(roomName, password);
        return RE_DIRECT;
    }

    @PostMapping(path = "/deleteRoom/{roomId}")
    public String deleteRoom(@PathVariable int roomId, @RequestParam String password) {
        roomService.deleteRoom(roomId, password);
        return RE_DIRECT;
    }
}
