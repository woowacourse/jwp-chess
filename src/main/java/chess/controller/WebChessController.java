package chess.controller;

import chess.dto.request.MoveRequestDto;
import chess.dto.request.RoomRequestDto;
import chess.dto.response.GameResponseDto;
import chess.dto.response.RoomResponseDto;
import chess.dto.response.RoomsResponseDto;
import chess.dto.response.StatusResponseDto;
import chess.service.ChessService;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public ResponseEntity<RoomResponseDto> createRoom(@RequestBody RoomRequestDto roomRequestDto) {
        final RoomResponseDto room = chessService.createRoom(roomRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .location(URI.create("/api/chess/rooms/" + room.getId()))
            .body(room);
    }

    @GetMapping
    public ResponseEntity<RoomsResponseDto> findRooms() {
        return ResponseEntity.status(HttpStatus.OK)
            .body(chessService.findRooms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameResponseDto> enterRoom(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(chessService.getCurrentBoards(id));
    }

    @PutMapping("/{id}/restart")
    public ResponseEntity<GameResponseDto> reenterRoom(@PathVariable Long id) {
        chessService.recreateRoom(id);
        return ResponseEntity.status(HttpStatus.OK)
            .body(chessService.getCurrentBoards(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RoomResponseDto> updateRoom(@PathVariable Long id,
                                                      @RequestBody RoomRequestDto roomRequestDto) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(chessService.updateRoom(id, roomRequestDto));
    }

    @PatchMapping("/{id}/end")
    public ResponseEntity<Void> endGame(@PathVariable Long id,
                                        @RequestBody RoomRequestDto roomRequestDto) {
        chessService.endRoom(id, roomRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
            .build();
    }

    @PatchMapping("/{id}/move")
    public ResponseEntity<GameResponseDto> movePiece(@PathVariable Long id,
                                                     @RequestBody MoveRequestDto moveRequestDto) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(chessService.move(id, moveRequestDto));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<StatusResponseDto> calculateStatus(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(chessService.calculateStatus(id));
    }
}
