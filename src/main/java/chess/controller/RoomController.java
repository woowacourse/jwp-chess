package chess.controller;

import chess.service.RoomService;
import chess.view.OutputView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/list")
    private String loadRoomList(Model model) {
        return OutputView.printMainPage(model, roomService.loadList());
    }

    @PostMapping("/create")
    private String createRoom(@RequestParam String roomName) {
        final Long roomId = roomService.save(roomName);
        return "redirect:/game/create/" + roomId;
    }

    @PostMapping("/delete")
    private String deleteRoom(@RequestParam Long roomId) {
        roomService.delete(roomId);
        return "redirect:/room/list";
    }
}