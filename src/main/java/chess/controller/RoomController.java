package chess.controller;

import chess.service.RoomService;
import chess.view.OutputView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    private void createRoom(@RequestParam String roomName, HttpServletResponse httpServletResponse) throws IOException {
        final Long roomId = roomService.save(roomName);
        httpServletResponse.sendRedirect("/game/create/" + roomId);
    }

    @GetMapping("/delete/{roomId}")
    private void deleteRoom(@PathVariable Long roomId, HttpServletResponse httpServletResponse) throws IOException {
        roomService.delete(roomId);
        httpServletResponse.sendRedirect("/room/list");
    }
}