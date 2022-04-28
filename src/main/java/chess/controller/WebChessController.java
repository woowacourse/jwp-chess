package chess.controller;

import chess.dto.StatusDto;
import chess.dto.request.MoveRequestDto;
import chess.dto.request.RoomRequestDto;
import chess.dto.response.GameResponseDto;
import chess.dto.response.RoomResponseDto;
import chess.dto.response.RoomsResponseDto;
import chess.service.ChessService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<RoomResponseDto> createRoom(@RequestBody RoomRequestDto roomRequestDto) {
        final RoomResponseDto room = chessService.createRoom(roomRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(room);
    }

    @GetMapping
    public ResponseEntity<RoomsResponseDto> findRooms() {

        return ResponseEntity.ok(chessService.findRooms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameResponseDto> getCurrentBoard(@PathVariable Long id) {

        return ResponseEntity.ok(chessService.getCurrentBoard(id));
    }

    @PostMapping("/{id}/move")
    public ResponseEntity<GameResponseDto> movePiece(@PathVariable Long id,
                                                     @RequestBody MoveRequestDto moveRequestDto) {
        return ResponseEntity.ok(chessService.move(id, moveRequestDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> finishGame(@PathVariable Long id) {
        chessService.endRoom(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<StatusDto> calculateStatus(@PathVariable Long id) {

        return ResponseEntity.ok(chessService.createStatus(id));
    }
}
