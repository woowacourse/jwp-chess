package chess.controller;

import chess.dto.*;
import chess.service.SpringChessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SpringChessRestController {

    private final SpringChessService springChessService;

    public SpringChessRestController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    @GetMapping("/rooms")
    public List<String> roomNames() {
        return springChessService.rooms();
    }

    @PostMapping("/rooms")
    public ResponseEntity<String> createRoom(@RequestBody RoomNameDto roomName) {
        springChessService.createRoom(roomName.getRoomName());
        return ResponseEntity.status(HttpStatus.CREATED).body("방 생성 성공!");
    }

    @PutMapping(value = "/rooms/{roomName}/move")
    public ResponseEntity<ResponseDto> move(@RequestBody PositionDto positionDto, @PathVariable String roomName) {
        return ResponseEntity.ok().body(springChessService.move(positionDto, roomName));
    }

    @PutMapping(value = "/rooms/{roomName}/restart")
    public void restart(@PathVariable String roomName) {
        springChessService.restartRoom(roomName);
    }

    @GetMapping("/rooms/{roomName}/board")
    public Map<String, String> currentBoard(@PathVariable String roomName) {
        return springChessService.currentBoardByRoomName(roomName);
    }

    @GetMapping(value = "/rooms/{roomName}/turn")
    public TurnDto currentTurn(@PathVariable String roomName) {
        return new TurnDto(springChessService.turnName(roomName));
    }

    @GetMapping(value = "/rooms/{roomName}/score")
    public ScoreDto score(@PathVariable String roomName) {
        return springChessService.score(roomName);
    }

    @DeleteMapping("/rooms/{roomName}")
    public ResponseEntity deleteRoom(@PathVariable String roomName) {
        springChessService.deleteRoom(roomName);
        return ResponseEntity.noContent().build();
    }
}
