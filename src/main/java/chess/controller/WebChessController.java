package chess.controller;

import chess.dto.StatusDto;
import chess.dto.request.MoveRequestDto;
import chess.dto.request.RoomRequestDto;
import chess.dto.response.GameResponseDto;
import chess.dto.response.RoomResponseDto;
import chess.dto.response.RoomsResponseDto;
import chess.entity.RoomEntity;
import chess.service.ChessService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chess/rooms")
public class WebChessController {

    private final ChessService chessService;

    public WebChessController(final ChessService chessService) {
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

    @GetMapping("/{id}/enter")
    public ResponseEntity<Object> enterRoom(@PathVariable() Long id) {
        final RoomEntity roomEntity = chessService.enterRoom(id);
        if (roomEntity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameResponseDto> getCurrentBoard(@PathVariable() Long id) {
        return ResponseEntity.ok(chessService.getCurrentBoard(id));
    }

    @PostMapping("/{id}/move")
    public ResponseEntity<GameResponseDto> movePiece(@PathVariable Long id,
                                                     @RequestBody MoveRequestDto moveRequestDto) {
        return ResponseEntity.ok(chessService.move(id, moveRequestDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StatusDto> finishGame(@PathVariable Long id) {
        chessService.endRoom(id);
        return ResponseEntity.ok(chessService.createStatus(id));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<StatusDto> calculateStatus(@PathVariable Long id) {
        return ResponseEntity.ok(chessService.createStatus(id));
    }
}
