package chess.controller;

import chess.dto.BoardDto;
import chess.dto.BoardStatusDto;
import chess.dto.MovablePositionDto;
import chess.dto.MoveRequestDto;
import chess.service.SpringChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SpringChessRestController {
    private final SpringChessService springChessService;

    public SpringChessRestController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    @GetMapping("/create/{id}")
    public ResponseEntity<BoardDto> createRoom(@PathVariable("id") String id) {
        return ResponseEntity.ok(springChessService.loadRoom(id));
    }

    @PostMapping("/move")
    public ResponseEntity<BoardDto> move(@RequestBody MoveRequestDto moveRequestDto) {
        try {
            return ResponseEntity.ok(springChessService.move(moveRequestDto));
        } catch (Exception e) {
            return ResponseEntity.ok(springChessService.loadRoom(moveRequestDto.getRoomId()));
        }
    }

    @PostMapping("/movable")
    public ResponseEntity<List<String>> movablePosition(@RequestBody MovablePositionDto movablePositionDto) {
        return ResponseEntity.ok(springChessService.movablePosition(movablePositionDto));
    }

    @PostMapping("/score/{roomId}")
    public ResponseEntity<BoardStatusDto> score(@PathVariable String roomId) {
        return ResponseEntity.ok(springChessService.boardStatusDto(roomId));
    }
}
