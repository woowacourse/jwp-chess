package chess.controller.web;

import chess.service.GameService;
import chess.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RoomController {
    private final RoomService roomService;
    private final GameService gameService;

    public RoomController(RoomService roomService, GameService gameService) {
        this.roomService = roomService;
        this.gameService = gameService;
    }

    @GetMapping("/main")
    public String loadRoomList(Model model) {
        model.addAttribute("list", roomService.loadList());
        return "mainPage";
    }

    @RequestMapping(value = "/room/new", method = RequestMethod.POST)
    public String createRoom(@RequestParam String roomName) {
        final Long roomId = roomService.save(roomName);
        gameService.create(roomId);
        return "redirect:/game/load/" + roomId;
    }

    @GetMapping("/room/delete/{roomId}")
    public String deleteRoom(@PathVariable Long roomId) {
        roomService.delete(roomId);
        gameService.delete(roomId);
        return "redirect:/main";
    }
}