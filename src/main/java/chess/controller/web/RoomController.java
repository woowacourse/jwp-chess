package chess.controller.web;

import chess.service.RequestHandler;
import chess.service.RoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import static spark.Spark.get;

@Controller
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/main")
    private String loadRoomList(Model model) throws SQLException {
        model.addAttribute("list", roomService.loadList());
        return "mainPage";
    }

    @GetMapping("/room/create/{roomName}")
    private void createRoom(@PathVariable String roomName, HttpServletResponse httpServletResponse) throws SQLException, IOException {
        final Long roomId = roomService.save(roomName);
        httpServletResponse.sendRedirect("/game/create/" + roomId);
    }

    @GetMapping("/room/delete/{roomId}")
    private void deleteRoom(@PathVariable Long roomId, HttpServletResponse httpServletResponse) throws SQLException, IOException {
        roomService.delete(roomId);
        httpServletResponse.sendRedirect("/room/delete/" + roomId);
    }
}