package chess.controller;

import chess.model.dto.CreateRoomDto;
import chess.model.dto.DeleteRoomDto;
import chess.service.RoomService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiWebController {

    private static final Gson GSON = new Gson();

    @Autowired
    private RoomService roomService;

    @GetMapping("/viewRooms")
    public String viewRooms() {
        return GSON.toJson(roomService.getUsedRooms());
    }

    @PostMapping("/createRoom")
    public String createRoom(@RequestBody String req) {
        roomService.addRoom(GSON.fromJson(req, CreateRoomDto.class));
        return GSON.toJson(roomService.getUsedRooms());
    }

    @PostMapping("/deleteRoom")
    public String deleteRoom(@RequestBody String req) {
        roomService.deleteRoom(GSON.fromJson(req, DeleteRoomDto.class));
        return GSON.toJson(roomService.getUsedRooms());
    }
}
