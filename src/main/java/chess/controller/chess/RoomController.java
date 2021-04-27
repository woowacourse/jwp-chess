package chess.controller.chess;

import chess.domain.dto.RoomDto;
import chess.domain.dto.RoomsDto;
import chess.domain.dto.move.MoveRequestDto;
import chess.domain.dto.move.MoveResponseDto;
import chess.serivce.chess.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    private final ChessService service;

    public RoomController(final ChessService service) {
        this.service = service;
    }

    @GetMapping("/")
    public ResponseEntity<RoomsDto> roomList() {
        RoomsDto result = service.findAll();
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/{name}")
    public ResponseEntity<RoomDto> createRoom(@PathVariable("name") String roomName) {
        RoomDto result = service.createRoom(roomName);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{name}")
    public ResponseEntity<MoveResponseDto> enterRoom(@PathVariable("name") String roomName) {
        MoveResponseDto result = service.findPiecesByRoomName(roomName);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/{name}/start")
    public ResponseEntity<MoveResponseDto> startRoom(@PathVariable("name") String roomName) {
        MoveResponseDto result = service.start(roomName);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/{name}/end")
    public ResponseEntity<MoveResponseDto> endRoom(@PathVariable("name") String roomName) {
        MoveResponseDto result = service.end(roomName);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/{name}/move")
    public ResponseEntity<MoveResponseDto> move(@PathVariable("name") String roomName, @RequestBody MoveRequestDto moveRequestDto) {
        MoveResponseDto result = service.move(roomName, moveRequestDto.getSource(),
            moveRequestDto.getTarget());
        return ResponseEntity.ok().body(result);
    }
}
