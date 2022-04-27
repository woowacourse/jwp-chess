package chess.controller;

import chess.dto.RoomDto;
import chess.entity.RoomEntity;
import chess.service.RoomService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class ChessRoomController {

    private final RoomService roomService;

    public ChessRoomController(final RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("")
    public String home() {
        return "home";
    }

    @ResponseBody
    @GetMapping("search")
    public List<RoomEntity> search() {
        return roomService.searchRooms();
    }

    @PostMapping("create")
    public void create(@RequestBody RoomDto roomDto) {
        roomService.createRoom(roomDto.getName(), roomDto.getPassword());
    }
}
