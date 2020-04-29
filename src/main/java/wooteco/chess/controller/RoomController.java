package wooteco.chess.controller;

import org.springframework.web.bind.annotation.*;
import wooteco.chess.dto.ChessResponseDto;
import wooteco.chess.dto.ResponseDto;
import wooteco.chess.dto.RoomDto;
import wooteco.chess.service.ChessService;
import wooteco.chess.service.RoomService;


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
        RoomDto roomDto = new RoomDto();
        roomDto.setWhiteUserId(userId);
        roomDto.setName(roomName);

        return roomService.create(roomDto);
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
    public ResponseDto<ChessResponseDto> renew(@PathVariable int roomId) throws Exception {
        return chessService.renew(roomId);
    }
}

