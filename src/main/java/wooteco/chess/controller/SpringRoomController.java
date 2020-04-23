package wooteco.chess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import wooteco.chess.domain.room.Room;
import wooteco.chess.service.RoomService;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/rooms")
public class SpringRoomController {
    // TODO: 2020/04/22 스프링 빈을 thread-safe 하게 만드려면 stateless여야 한다. 필드를 제거할 방법을 찾아보자.
    private final RoomService roomService;

    private SpringRoomController(final RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public String getAllRooms(Model model) throws SQLException {
        List<Room> rooms = roomService.findAllRoom();
        model.addAttribute("rooms", rooms);

        return "index";
    }

    @GetMapping("/enter")
    public String enterRoom(@RequestParam(value = "roomId") Integer roomId, Model model) throws SQLException {
        model.addAttribute("roomId", roomId);
        return "game";
    }

    // TODO: 2020/04/22 valid 에러페이지 이동 문제
    @GetMapping("/create")
    public String createRoom(@Valid RoomName roomName, Errors errors, Model model) throws SQLException {
        if(errors.hasErrors()){
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
