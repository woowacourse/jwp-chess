package chess.controller.api;

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
public class RoomController {

    private final RoomService roomService;
    private final ChessGameService chessGameService;

    public RoomController(RoomService roomService, ChessGameService chessGameService) {
        this.roomService = roomService;
        this.chessGameService = chessGameService;
    }

    @GetMapping("/rooms")
    public RoomsDto getRooms() {
        return roomService.getUsedRooms();
    }

    @PostMapping("/room")
    public RoomsDto postRoom(@RequestBody CreateRoomDto createRoomDto) {
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
