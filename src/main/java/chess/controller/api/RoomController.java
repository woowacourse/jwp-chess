package chess.controller.api;

import chess.dto.repository.RoomsDto;
import chess.dto.view.CreateRoomDto;
import chess.dto.view.DeleteRoomDto;
import chess.service.ChessGameService;
import chess.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<RoomsDto> getRooms() {
        RoomsDto roomsDto = roomService.getUsedRooms();
        return new ResponseEntity<>(roomsDto, HttpStatus.OK);
    }

    @PostMapping("/room")
    public ResponseEntity<RoomsDto> createRoom(@RequestBody CreateRoomDto createRoomDto) {
        roomService.addRoom(createRoomDto);
        RoomsDto roomsDto = roomService.getUsedRooms();
        return new ResponseEntity<>(roomsDto, HttpStatus.OK);
    }

    @DeleteMapping("/room")
    public ResponseEntity<RoomsDto> deleteRoom(@RequestBody DeleteRoomDto deleteRoomDto) {
        roomService.deleteRoom(deleteRoomDto);
        chessGameService.findProceedGameId(deleteRoomDto.getRoomId())
            .ifPresent(chessGameService::closeGame);
        RoomsDto roomsDto = roomService.getUsedRooms();
        return new ResponseEntity<>(roomsDto, HttpStatus.OK);
    }
}
