package chess.controller;

import chess.exceptions.DuplicateRoomException;
import chess.exceptions.NoRoomException;
import chess.domain.dto.RoomDto;
import chess.domain.dto.RoomsDto;
import chess.domain.dto.move.MoveRequestDto;
import chess.domain.dto.move.MoveResponseDto;
import chess.serivce.chess.ChessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/room")
public class ChessRoomController {

    private final ChessService service;

    public ChessRoomController(final ChessService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<RoomDto> createRoom(@RequestBody RoomDto roomDto) {
        try {
            service.createRoom(roomDto.getName());
            return ResponseEntity.ok().body(roomDto);
        } catch (DuplicateRoomException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
    }

    @GetMapping(value = "/{name}")
    public ResponseEntity<MoveResponseDto> enterRoom(@PathVariable("name") String roomName) {
        try {
            MoveResponseDto result = service.findPiecesInRoom(roomName);
            return ResponseEntity.ok().body(result);
        } catch (NoRoomException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
    }

    @GetMapping(value = "/{name}/end")
    public ResponseEntity<MoveResponseDto> endRoom(@PathVariable("name") String roomName) {
        MoveResponseDto result = service.end(roomName);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping(value = "/{name}/move")
    public ResponseEntity<MoveResponseDto> move(@PathVariable("name") String roomName, @RequestBody MoveRequestDto moveRequestDto) {
        MoveResponseDto result = service.move(roomName, moveRequestDto.getSource(),
                moveRequestDto.getTarget());
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<RoomsDto> showRooms() {
        RoomsDto result = service.findAllRooms();
        return ResponseEntity.ok().body(result);
    }
}
