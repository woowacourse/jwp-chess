package chess.controller.web;

import chess.service.RequestHandler;
import chess.service.RoomService;
import org.springframework.stereotype.Controller;
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
    private ModelAndView loadRoomList() throws SQLException {
        ModelAndView modelAndView = new ModelAndView("mainPage");
        modelAndView.addObject(roomService.loadList());
        return modelAndView;
    }

    @GetMapping("/room/create/{roomName}")
    private void createRoom(@PathVariable String roomName, HttpServletResponse httpServletResponse) throws SQLException, IOException {
        final Long roomId = roomService.save(roomName);
        httpServletResponse.sendRedirect("/game/create/" + roomId);
    }

    private void deleteRoom() {
        get("/room/delete/:roomId", (req, res) -> {
            final Long roomId = RequestHandler.roomId(req);
            roomService.delete(roomId);

            res.status(200);
            res.redirect("/game/delete/" + roomId);
            return null;
        });
    }
}