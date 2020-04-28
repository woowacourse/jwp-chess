package wooteco.chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.room.Room;
import wooteco.chess.service.RoomService;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Controller
public class RoomController {

    private final RoomService roomService;

    public RoomController(final RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/")
    public String index(Model model) throws SQLException {
        List<Room> rooms = roomService.findAllRoom();
        model.addAttribute("rooms", rooms);
        return "index";
    }

    @PostMapping("/room")
    public String create(
            @RequestParam String title,
            Model model) throws SQLException {
        Long id = roomService.findCurrentMaxId() + 1;
        roomService.createRoom(id, title);
        Map<String, String> board = roomService.initializeBoard(id);
        model.addAttribute("board", board);
        return "redirect:" + "/room/" + id ;
    }

    @GetMapping("/room/{room_id}")
    public String room(
            @PathVariable("room_id") Long id,
            Model model) throws SQLException {
        Map<String, String> board = roomService.findPiecesById(id);
        model.addAttribute("board", board);
        model.addAttribute("roomId", id);
        return "room";
    }

    @PostMapping("/room/{room_id}")
    public String finish(
            @PathVariable("room_id") Long roomId
    ) throws SQLException {
        roomService.deleteRoom(roomId);
        return "redirect:" + "/";
    }

    @GetMapping("/room/{room_id}/turn")
    @ResponseBody
    public String turn(
            @PathVariable("room_id") Long roomId
    ) throws SQLException {
        Team currentTurn = roomService.getCurrentTurn(roomId);
        return currentTurn.name();
    }
}
