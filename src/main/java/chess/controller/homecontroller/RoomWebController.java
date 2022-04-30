package chess.controller.homecontroller;

import chess.dto.RoomCreationRequestDto;
import chess.dto.RoomDeletionRequestDto;
import chess.dto.RoomDto;
import chess.service.RoomService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @PostMapping(path = "/")
    public String makeRoom(@RequestBody RoomCreationRequestDto roomCreationRequest) {
        final String roomName = roomCreationRequest.getRoomName();
        final String password = roomCreationRequest.getPassword();
        roomService.saveNewRoom(roomName, password);
        return RE_DIRECT;
    }

    @ResponseBody
    @DeleteMapping(path = "/")
    public String deleteRoom(@RequestBody RoomDeletionRequestDto roomDeletionRequest) {

        final int roomId = roomDeletionRequest.getRoomId();
        final String password = roomDeletionRequest.getPassword();
        roomService.deleteRoom(roomId, password);
        return RE_DIRECT;
    }
}
