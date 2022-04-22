package chess.controller;

import chess.dto.StatusDto;
import chess.dto.request.MoveRequestDto;
import chess.dto.request.RoomRequestDto;
import chess.dto.response.GameResponseDto;
import chess.dto.response.RoomResponseDto;
import chess.dto.response.RoomsResponseDto;
import chess.service.ChessService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chess")
public class SpringChessController {

    private final ChessService chessService;

    public SpringChessController(final ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("/rooms")
    public ResponseEntity<?> createRoom(@RequestBody RoomRequestDto roomRequestDto) {
        final RoomResponseDto room = chessService.createRoom(roomRequestDto);
        return ResponseEntity.created(URI.create("/api/chess/rooms/" + room.getId())).build();
    }

    @GetMapping("/rooms")
    public ResponseEntity<RoomsResponseDto> findRooms() {
        final RoomsResponseDto rooms = chessService.findRooms();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/rooms/{id}")
    public ResponseEntity<GameResponseDto> enterRoom(@PathVariable() Long id) {
        return ResponseEntity.ok(chessService.enterRoom(id));
    }

    @DeleteMapping("/rooms/{id}")
    public ResponseEntity<StatusDto> finishGame(@PathVariable Long id) {
        chessService.endRoom(id);
        return ResponseEntity.ok(chessService.createStatus(id));
    }

    @PostMapping("/rooms/{id}/move")
    public ResponseEntity<GameResponseDto> movePiece(@PathVariable Long id,
                                                     @RequestBody MoveRequestDto moveRequestDto) {
        return ResponseEntity.ok(chessService.move(id, moveRequestDto));
    }
}
