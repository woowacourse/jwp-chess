package chess.controller;

import chess.dto.request.MoveRequestDto;
import chess.dto.request.RoomDeleteRequestDto;
import chess.dto.request.RoomRequestDto;
import chess.dto.response.PieceResponseDto;
import chess.dto.response.RoomResponseDto;
import chess.dto.response.ScoreResponseDto;
import chess.service.ChessService;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rooms")
public class ChessApiController {

    private final ChessService chessService;

    public ChessApiController(final ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping
    public ResponseEntity<List<RoomResponseDto>> loadAllRoom() {
        final List<RoomResponseDto> rooms = chessService.loadAllRoom();
        return ResponseEntity.ok(rooms);
    }

    @PostMapping
    public ResponseEntity<RoomResponseDto> createRoom(@RequestBody final RoomRequestDto roomRequestDto) {
        final RoomResponseDto roomResponseDto = chessService.createRoom(roomRequestDto);
        return ResponseEntity.created(URI.create("/rooms/" + roomResponseDto.getId()))
                .body(roomResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponseDto> loadRoom(@PathVariable long id) {
        final RoomResponseDto room = chessService.loadRoom(id);
        return ResponseEntity.ok(room);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable final long id,
                                        @RequestBody final RoomDeleteRequestDto roomDeleteRequestDto) {
        chessService.deleteRoom(id, roomDeleteRequestDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/pieces")
    public ResponseEntity<List<PieceResponseDto>> loadAllPiece(@PathVariable long id) {
        final List<PieceResponseDto> pieces = chessService.loadAllPiece(id);
        return ResponseEntity.ok(pieces);
    }

    @PatchMapping("/{id}/pieces")
    public ResponseEntity<?> movePiece(@PathVariable long id, @RequestBody final MoveRequestDto moveRequestDto) {
        chessService.movePiece(id, moveRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/scores")
    public ResponseEntity<ScoreResponseDto> loadScore(@PathVariable final long id) {
        final ScoreResponseDto scoreResponseDto = chessService.loadScore(id);
        return ResponseEntity.ok(scoreResponseDto);
    }

    @PatchMapping("/{id}/end")
    public ResponseEntity<?> endGame(@PathVariable final long id) {
        chessService.end(id);
        return ResponseEntity.ok().build();
    }
}
