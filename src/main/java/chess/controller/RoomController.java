package chess.controller;

import chess.service.GameService;
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
    private final GameService gameService;

    public RoomController(final RoomService roomService, final GameService gameService) {
        this.roomService = roomService;
        this.gameService = gameService;
    }

    @GetMapping("/list")
    private String loadRoomList(final Model model) {
        return OutputView.printMainPage(model, roomService.loadList());
    }

    @PostMapping("/create")
    private String createRoom(final String roomName) {
        final Long roomId = roomService.save(roomName);
        return "redirect:/game/create/" + roomId;
    }

    @PostMapping("/delete")
    private String deleteRoom(final Long roomId) {
        roomService.delete(roomId);
        gameService.delete(roomId);
        return "redirect:/room/list";
    }
}