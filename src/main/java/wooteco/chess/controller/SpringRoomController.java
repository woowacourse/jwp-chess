package wooteco.chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wooteco.chess.domain.room.Room;
import wooteco.chess.dto.RoomName;
import wooteco.chess.service.SpringRoomService;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/rooms")
public class SpringRoomController {

    private final SpringRoomService roomService;

    private SpringRoomController(final SpringRoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public String getAllRooms(Model model) throws SQLException {
        List<Room> rooms = roomService.findAllRoom();
        model.addAttribute("rooms", rooms);

        return "index";
    }

    @GetMapping("/enter")
    public String enterRoom(@RequestParam(value = "roomId") Integer roomId, Model model) {
        model.addAttribute("roomId", roomId);
        return "game";
    }

    // TODO: 2020/04/22 valid 에러페이지 이동 문제
    @GetMapping("/create")
    public String createRoom(@Valid RoomName roomName, Errors errors, Model model) throws SQLException {
        if (errors.hasErrors()) {
            model.addAttribute("errors", errors);
            return getAllRooms(model);
        }
        roomService.addRoom(roomName.getRoomName());
        return "redirect:/rooms";
    }

    @GetMapping("/remove")
    public String removeRoom(@RequestParam(value = "roomId") Integer roomId) throws SQLException {
        roomService.removeRoom(roomId);
        return "redirect:/rooms";
    }
}
