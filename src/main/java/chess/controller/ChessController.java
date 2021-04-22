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
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessController {

    private final ChessService service;

    public ChessController(final ChessService service) {
        this.service = service;
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity exceptionHandler(RuntimeException e) {
        return ResponseEntity.ok().body(ResponseDto.error(e.getMessage()));
    }

    @GetMapping(value = "/createroom/{name}")
    public ResponseEntity<RoomDto> createRoom(@PathVariable("name") String roomName) throws SQLException {
        service.createRoom(roomName);
        return ResponseEntity.ok().body(new RoomDto(roomName));
    }

    @GetMapping(value = "/room/{name}")
    public ResponseEntity<MoveResponseDto> enterRoom(@PathVariable("name") String roomName) throws SQLException {
        MoveResponseDto result = service.findPiecesByRoomName(roomName);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/room/{name}/start")
    public ResponseEntity<MoveResponseDto> startRoom(@PathVariable("name") String roomName) throws SQLException {
        MoveResponseDto result = service.start(roomName);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/room/{name}/end")
    public ResponseEntity<MoveResponseDto> endRoom(@PathVariable("name") String roomName) throws SQLException {
        MoveResponseDto result = service.end(roomName);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping(value = "/room/{name}/move")
    public ResponseEntity<MoveResponseDto> move(@PathVariable("name") String roomName, @RequestBody MoveRequestDto moveRequestDto)
            throws SQLException {
        MoveResponseDto result = service.move(roomName, moveRequestDto.getSource(),
                moveRequestDto.getTarget());
        return ResponseEntity.ok().body(result);
    }
}
