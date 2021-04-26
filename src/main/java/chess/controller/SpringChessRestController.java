package chess.controller;

import chess.dto.*;
import chess.service.SpringChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/rooms")
@RestController
public class SpringChessRestController {
    private final SpringChessService springChessService;

    public SpringChessRestController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    @GetMapping("/{id}/board")
    public ResponseEntity<BoardDto> createRoom(@PathVariable("id") String id) {
        return ResponseEntity.ok(springChessService.loadRoom(id));
    }

    @PostMapping("/{id}/command")
    public ResponseEntity<BoardDto> move(@PathVariable("id") String id, @RequestBody MoveRequestDto moveRequestDto) {
        try {
            return ResponseEntity.ok(springChessService.move(id, moveRequestDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(springChessService.loadRoom(id));
        }
    }

    @GetMapping("{id}/path")
    public ResponseEntity<List<String>> movablePosition(@PathVariable("id") String roomId, @RequestParam("target") String target) {
        MovablePositionDto dto = new MovablePositionDto(roomId, target);
        return ResponseEntity.ok(springChessService.movablePosition(dto));
    }

    @GetMapping("{id}/status")
    public ResponseEntity<BoardStatusDto> score(@PathVariable("id") String roomId) {
        return ResponseEntity.ok(springChessService.boardStatusDto(roomId));
    }

    @GetMapping
    public ResponseEntity<List<RoomDto>> isExistingRoom() {
        return ResponseEntity.ok(springChessService.roomIds());
    }
}
