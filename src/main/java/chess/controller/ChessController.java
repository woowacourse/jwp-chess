package chess.controller;

import chess.dto.response.StatusResponseDto;
import chess.dto.request.MoveRequestDto;
import chess.dto.request.RoomRequestDto;
import chess.dto.response.GameResponseDto;
import chess.dto.response.RoomResponseDto;
import chess.dto.response.RoomsResponseDto;
import chess.service.ChessService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chess/rooms")
public class ChessController {

    private final ChessService chessService;

    public ChessController(final ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody RoomRequestDto roomRequestDto) {
        final RoomResponseDto room = chessService.createRoom(roomRequestDto);
        return ResponseEntity.created(URI.create("/api/chess/rooms/" + room.getId())).build();
    }

    @GetMapping
    public ResponseEntity<RoomsResponseDto> findRooms() {
        return ResponseEntity.ok(chessService.findRooms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameResponseDto> enterRoom(@PathVariable Long id) {
        return ResponseEntity.ok(chessService.enterRoom(id));
    }

    @PatchMapping("/{id}/end")
    public ResponseEntity<StatusResponseDto> finishGame(@PathVariable Long id) {
        chessService.endRoom(id);
        return ResponseEntity.ok(chessService.createStatus(id));
    }

    @PostMapping("/{id}/move")
    public ResponseEntity<GameResponseDto> movePiece(@PathVariable Long id,
                                                     @RequestBody MoveRequestDto moveRequestDto) {
        return ResponseEntity.ok(chessService.move(id, moveRequestDto));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<StatusResponseDto> calculateStatus(@PathVariable Long id) {
        return ResponseEntity.ok(chessService.createStatus(id));
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<?> changeRoomName(@PathVariable Long id,
                                         @RequestBody RoomRequestDto roomRequestDto) {
        chessService.updateRoomName(id, roomRequestDto.getName());
        return ResponseEntity.ok().build();
    }
}
