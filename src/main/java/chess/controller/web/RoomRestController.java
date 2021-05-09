package chess.controller.web;

import chess.controller.web.dto.game.RoomIdAndGameIdResponse;
import chess.controller.web.dto.game.RoomJoinRequestDto;
import chess.controller.web.dto.game.RoomRequestDto;
import chess.controller.web.dto.room.PlayingRoomResponseDto;
import chess.controller.web.dto.room.RoomIdResponseDto;
import chess.service.RoomService;
import chess.service.dto.RoomAndGameIdDto;
import chess.service.dto.game.RoomJoinDto;
import chess.service.dto.game.RoomSaveDto;
import chess.service.dto.room.PlayingRoomDto;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rooms")
public class RoomRestController {

    private final RoomService roomService;
    private final ModelMapper modelMapper;

    public RoomRestController(RoomService roomService, ModelMapper modelMapper) {
        this.roomService = roomService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<RoomIdAndGameIdResponse> createRoom(@RequestBody RoomRequestDto roomRequestDto,
                                                              HttpSession httpSession) {
        RoomSaveDto roomSaveDto = modelMapper.map(roomRequestDto, RoomSaveDto.class);
        httpSession.setAttribute("password", roomSaveDto.getWhitePassword());
        RoomAndGameIdDto roomAndGameIdDto = roomService.saveRoom(roomSaveDto);
        return ResponseEntity.ok().body(modelMapper.map(roomAndGameIdDto, RoomIdAndGameIdResponse.class));
    }

    @GetMapping("/playing")
    public ResponseEntity<List<PlayingRoomResponseDto>> findPlayingGames() {
        List<PlayingRoomDto> playingRoomDtos = roomService.findRoomsByPlaying();
        List<PlayingRoomResponseDto> responseDtos = playingRoomDtos.stream()
                .map(playingRoomDto -> modelMapper.map(playingRoomDto, PlayingRoomResponseDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(responseDtos);
    }

    @PostMapping("/{roomId}/join")
    public ResponseEntity<RoomIdResponseDto> joinRoom(@PathVariable Long roomId,
                                                      @RequestBody RoomJoinRequestDto roomJoinRequestDto,
                                                      HttpSession httpSession) {
        RoomJoinDto roomJoinDto = modelMapper.map(roomJoinRequestDto, RoomJoinDto.class);
        httpSession.setAttribute("password", roomJoinDto.getPassword());
        return ResponseEntity.ok().body(new RoomIdResponseDto(roomService.joinGame(roomId, roomJoinDto)));
    }
}
