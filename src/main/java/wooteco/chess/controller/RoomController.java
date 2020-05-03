package wooteco.chess.controller;

import org.springframework.web.bind.annotation.*;
import wooteco.chess.dto.ResponseDto;
import wooteco.chess.entity.Room;
import wooteco.chess.service.ChessService;
import wooteco.chess.service.RoomService;

import java.util.List;


@RestController
@RequestMapping("/room")
public class RoomController {
    private RoomService roomService;

    public RoomController(RoomService roomService)
    {
        this.roomService = roomService;
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseDto create(@RequestParam String roomName,
                              @RequestParam String userPassword){
        Room room = new Room();
        room.setWhitePassword(userPassword);
        room.setName(roomName);

        return roomService.create(room);
    }

    @PostMapping("/join")
    @ResponseBody
    public ResponseDto join(@RequestParam String roomName,
                            @RequestParam String userPassword){

        return roomService.join(roomName, userPassword);
    }

    @PostMapping("/exit")
    @ResponseBody
    public ResponseDto exit(@RequestParam Long roomId,
                            @RequestParam String userPassword){
        return roomService.exit(roomId, userPassword);
    }

    @GetMapping("/status/{roomId}")
    @ResponseBody
    public ResponseDto status(@PathVariable Long roomId){
        return roomService.status(roomId);
    }

    @GetMapping("/list")
    @ResponseBody
    public ResponseDto<List<String>> list() {
        return roomService.getRooms();
    }
}

