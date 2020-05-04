package wooteco.chess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wooteco.chess.repository.RoomEntity;
import wooteco.chess.service.RoomService;

import java.util.Map;

@Controller
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(final RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("rooms", roomService.findAllRoom());
        return "index";
    }

    @PostMapping("/room")
    public String create(
            @RequestParam String title, Model model) {
        RoomEntity created = roomService.createRoom(title);
        Long roomId = created.getId();
        Map<String, String> board = roomService.initializeBoard(roomId);
        model.addAttribute("board", board);
        return "redirect:/room/" + roomId;
    }

    @GetMapping("/room/{room_id}")
    public String room(
            @PathVariable("room_id") Long roomId, Model model) {
        Map<String, String> board = roomService.findPiecesById(roomId);
        model.addAttribute("board", board);
        model.addAttribute("roomId", roomId);
        model.addAttribute("title", roomService.findTitleById(roomId));
        return "room";
    }

    @PostMapping("/room/{room_id}")
    public String finish(
            @PathVariable("room_id") Long roomId) {
        roomService.deleteRoom(roomId);
        return "redirect:/";
    }

    @GetMapping("/room/{room_id}/turn")
    @ResponseBody
    public String turn(
            @PathVariable("room_id") Long roomId) {
        return roomService.findTurnById(roomId);
    }

    @GetMapping("/room/{room_id}/reset")
    public String reset(
            @PathVariable("room_id") Long roomId, Model model) {
        model.addAttribute("board", roomService.resetRoom(roomId));
        model.addAttribute("roomId", roomId);
        model.addAttribute("title", roomService.findTitleById(roomId));
        return "room";
    }
}
