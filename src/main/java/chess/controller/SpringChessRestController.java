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

    @PostMapping("/create/{id}")
    public ResponseEntity<BoardDto> createRoom(@PathVariable("id") String id) {
        return ResponseEntity.ok(springChessService.loadRoom(id));
    }

    @PostMapping("/move")
    public ResponseEntity<BoardDto> move(@RequestBody MoveRequestDto moveRequestDto) {
        System.out.println("moveRequest : " + moveRequestDto.getRoomId() + moveRequestDto.getTarget() + moveRequestDto.getDestination());
        try {
            return ResponseEntity.ok(springChessService.move(moveRequestDto));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(springChessService.loadRoom(moveRequestDto.getRoomId()));
        }
    }

    @GetMapping("/movable")
    public ResponseEntity<List<String>> movablePosition(@RequestParam("roomId") String roomId, @RequestParam("target") String target) {
        MovablePositionDto dto = new MovablePositionDto(roomId, target);
        return ResponseEntity.ok(springChessService.movablePosition(dto));
    }

    @GetMapping("/score")
    public ResponseEntity<BoardStatusDto> score(String roomId) {
        return ResponseEntity.ok(springChessService.boardStatusDto(roomId));
    }
}
