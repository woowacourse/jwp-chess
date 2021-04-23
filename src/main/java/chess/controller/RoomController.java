package chess.controller;

import chess.domain.dto.ResponseDto;
import chess.domain.dto.RoomDto;
import chess.domain.dto.move.MoveRequestDto;
import chess.domain.dto.move.MoveResponseDto;
import chess.serivce.chess.ChessService;
import java.sql.SQLException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/room")
public class RoomController {
    private final ChessService service;

    public RoomController(final ChessService service) {
        this.service = service;
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity exceptionHandler(RuntimeException e) {
        return ResponseEntity.ok().body(ResponseDto.error(e.getMessage()));
    }

    @PostMapping("/create")
    public ResponseEntity<RoomDto> createRoom(@RequestBody RoomDto roomDto) {
        service.createRoom(roomDto.getName());
        return ResponseEntity.ok().body(roomDto);
    }

    @GetMapping("/{name}")
    public ResponseEntity<MoveResponseDto> enterRoom(@PathVariable("name") String roomName) {
        MoveResponseDto result = service.findPiecesByRoomName(roomName);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{name}/start")
    public ResponseEntity<MoveResponseDto> startRoom(@PathVariable("name") String roomName) {
        MoveResponseDto result = service.start(roomName);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{name}/end")
    public ResponseEntity<MoveResponseDto> endRoom(@PathVariable("name") String roomName) {
        MoveResponseDto result = service.end(roomName);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/{name}/move")
    public ResponseEntity<MoveResponseDto> move(@PathVariable("name") String roomName, @RequestBody MoveRequestDto moveRequestDto) {
        MoveResponseDto result = service.move(roomName, moveRequestDto.getSource(),
            moveRequestDto.getTarget());
        return ResponseEntity.ok().body(result);
    }
}
