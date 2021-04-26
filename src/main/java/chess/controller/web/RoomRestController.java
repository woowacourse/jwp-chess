package chess.controller.web;

import chess.chessgame.domain.room.game.ChessGameManager;
import chess.controller.web.dto.ActiveRoomsResponseDto;
import chess.controller.web.dto.ChessGameResponseDto;
import chess.controller.web.dto.RoomRequestDto;
import chess.service.ChessService;
import chess.service.RoomService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/room")
@RestController
public class RoomRestController {
    private final ChessService chessService;
    private final RoomService roomService;

    public RoomRestController(ChessService chessService, RoomService roomService) {
        this.chessService = chessService;
        this.roomService = roomService;
    }

    @GetMapping
    public ResponseEntity<ActiveRoomsResponseDto> getRooms() {
        return ResponseEntity.ok(new ActiveRoomsResponseDto(roomService.findAllRunningRoom()));
    }

    @PostMapping
    public ResponseEntity<Void> createRoom(@RequestBody RoomRequestDto roomRequestDto) {
        roomService.createRoom(roomRequestDto.getRoomName(), roomRequestDto.getWhiteUserPassword());
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{roomId:[\\d]+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChessGameResponseDto> enterRoom(@PathVariable long roomId) {
        ChessGameManager load = chessService.load(roomId);
        return ResponseEntity.ok(new ChessGameResponseDto(load));
    }
}
