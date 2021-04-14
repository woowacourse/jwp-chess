package chess.controller.web;

import chess.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/main")
    private String loadRoomList(Model model) {
        model.addAttribute("list", roomService.loadList());
        return "mainPage";
    }

    @GetMapping("/room/create/{roomName}")
    private void createRoom(@PathVariable String roomName, HttpServletResponse httpServletResponse) throws IOException {
        final Long roomId = roomService.save(roomName);
        httpServletResponse.sendRedirect("/game/create/" + roomId);
    }

    @GetMapping("/room/delete/{roomId}")
    private void deleteRoom(@PathVariable Long roomId, HttpServletResponse httpServletResponse) throws IOException {
        roomService.delete(roomId);
        httpServletResponse.sendRedirect("/main");
    }
}