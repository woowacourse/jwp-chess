package wooteco.chess.controller;

import org.springframework.web.bind.annotation.*;
import wooteco.chess.dto.ChessResponseDto;
import wooteco.chess.dto.ResponseDto;
import wooteco.chess.entity.Room;
import wooteco.chess.service.ChessService;
import wooteco.chess.service.RoomService;
import wooteco.chess.utils.IdGenerator;


@RestController
@RequestMapping("/room")
public class RoomController {
    private RoomService roomService;
    private ChessService chessService;

    public RoomController(RoomService roomService, ChessService chessService)
    {
        this.roomService = roomService;
        this.chessService = chessService;
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseDto create(@RequestParam String roomName,
                              @RequestParam Long userId) throws Exception {
        Room room = new Room();
        room.setRoomId(IdGenerator.generateRoomId());
        room.setWhiteUserId(userId);
        room.setName(roomName);

        return roomService.create(room);
    }

    @PostMapping("/join")
    @ResponseBody
    public ResponseDto join(@RequestParam String roomName,
                            @RequestParam Long userId) throws Exception {

        return roomService.join(roomName, userId);
    }

    @PostMapping("/exit")
    @ResponseBody
    public ResponseDto exit(@RequestParam Long roomId,
                            @RequestParam Long userId) throws Exception {
        return roomService.exit(roomId, userId);
    }

    @GetMapping("/status/{roomId}")
    @ResponseBody
    public ResponseDto status(@PathVariable Long roomId) throws Exception {
        return roomService.status(roomId);
    }

    @GetMapping("/renew/{roomId}")
    @ResponseBody
    public ResponseDto<ChessResponseDto> renew(@PathVariable Long roomId) throws Exception {
        return chessService.renew(roomId);
    }
}

