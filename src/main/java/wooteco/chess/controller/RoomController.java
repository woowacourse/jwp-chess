package wooteco.chess.controller;

import org.springframework.web.bind.annotation.*;
import wooteco.chess.dto.RequestDto.RoomExitRequestDto;
import wooteco.chess.dto.RequestDto.RoomJoinRequestDto;
import wooteco.chess.dto.ResponseDto.ResponseDto;
import wooteco.chess.dto.RequestDto.RoomCreateRequestDto;
import wooteco.chess.entity.Room;
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
    public ResponseDto create(@RequestBody final RoomCreateRequestDto requestDto){
        ResponseDto responseDto = roomService.create(requestDto);
        if(responseDto.getResponseCode() == 200){
            Long roomId = (Long)(responseDto.getResponseData());
            responseDto = ResponseDto.success(roomId.toString());
        }
        return responseDto;
    }

    @PostMapping("/join")
    @ResponseBody
    public ResponseDto<String> join(@RequestBody final RoomJoinRequestDto requestDto){
        Room room = (Room)(roomService.join(requestDto).getResponseData());
        return ResponseDto.success(room.getId().toString());
    }

    @PostMapping("/exit")
    @ResponseBody
    public ResponseDto exit(@RequestBody final RoomExitRequestDto requestDto){
        return roomService.exit(requestDto);
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

