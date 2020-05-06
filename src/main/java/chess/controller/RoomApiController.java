package chess.controller;

import chess.dto.CreateRoomDto;
import chess.dto.DeleteRoomDto;
import chess.dto.RoomsDto;
import chess.service.ChessGameService;
import chess.service.RoomService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RoomApiController {

    private final ChessGameService chessGameService;
    private final RoomService roomService;

    public RoomApiController(ChessGameService chessGameService, RoomService roomService) {
        this.chessGameService = chessGameService;
        this.roomService = roomService;
    }

    @GetMapping("/rooms")
    public RoomsDto viewRooms() {
        return roomService.getUsedRooms();
    }

    @PostMapping("/room")
    public RoomsDto createRoom(@RequestBody CreateRoomDto createRoomDto) {
        roomService.addRoom(createRoomDto);
        return roomService.getUsedRooms();
    }

    @DeleteMapping("/room")
    public RoomsDto deleteRoom(@RequestBody DeleteRoomDto deleteRoomDto) {
        roomService.deleteRoom(deleteRoomDto);
        chessGameService.findProceedGameId(deleteRoomDto.getRoomId())
            .ifPresent(chessGameService::closeGame);

        return roomService.getUsedRooms();
    }

}
