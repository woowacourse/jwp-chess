package wooteco.chess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import wooteco.chess.domain.room.Room;
import wooteco.chess.service.RoomService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping
    public ModelAndView index(ModelAndView model) {
        List<Room> rooms = roomService.findAllRoom();
        model.addObject("rooms", rooms);
        model.setViewName("room");
        return model;
    }

    @PostMapping
    public ModelAndView create(
            @RequestParam String title,
            ModelAndView model,
            HttpServletResponse response) throws IOException {
        Room created = roomService.createRoom(title);
        Long roomId = created.getId();
        Map<String, String> board = roomService.initializeBoard(roomId);
        model.addObject("board", board);
        response.sendRedirect("/room/" + roomId);
        return model;
    }

    @GetMapping("/{room_id}")
    public ModelAndView room(
            @PathVariable("room_id") Long roomId,
            ModelAndView model) {
        Map<String, String> board = roomService.findPiecesById(roomId);
        model.addObject("board", board);
        model.addObject("roomId", roomId);
        model.addObject("title", roomService.findTitleById(roomId));
        model.setViewName("board");
        return model;
    }

    @PostMapping("/{room_id}")
    public void finish(
            @PathVariable("room_id") Long roomId,
            HttpServletResponse response
    ) throws IOException {
        roomService.deleteRoom(roomId);
        response.sendRedirect("/room");
    }

    @GetMapping("/{room_id}/turn")
    public String turn(
            @PathVariable("room_id") Long roomId
    ) {
        return roomService.findTurnById(roomId);
    }

    @GetMapping("/{room_id}/reset")
    public ModelAndView reset(
            @PathVariable("room_id") Long roomId,
            ModelAndView model
    ) {
        model.addObject("board", roomService.resetRoom(roomId));
        model.addObject("roomId", roomId);
        model.addObject("title", roomService.findTitleById(roomId));
        model.setViewName("board");
        return model;
    }
}
