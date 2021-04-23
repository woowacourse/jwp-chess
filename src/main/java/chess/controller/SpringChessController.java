package chess.controller;

import chess.dto.*;
import chess.exception.ChessException;
import chess.exception.NotExistRoomException;
import chess.service.SpringChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class SpringChessController {

    private SpringChessService springChessService;

    public SpringChessController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/rooms")
    @ResponseBody
    public List<String> roomNames() {
        return springChessService.rooms();
    }

    @GetMapping("/rooms/{roomName}")
    public String enterRoom() {
        return "/chess.html";
    }

    @PostMapping("/rooms")
    @ResponseBody
    public void createRoom(@RequestBody RoomNameDto roomName) {
        springChessService.createRoom(roomName.getRoomName());
    }

    @PutMapping(value = "/rooms/{roomName}/move")
    @ResponseBody
    public ResponseEntity<ResponseDto> move(@RequestBody PositionDto positionDto, @PathVariable String roomName) {

        return ResponseEntity.ok().body(springChessService.move(positionDto, roomName));
    }

    @PutMapping(value = "/rooms/{roomName}/restart")
    @ResponseBody
    public void restart(@PathVariable String roomName) {
        springChessService.restartBoard(roomName);
    }

    @GetMapping("/rooms/{roomName}/board")
    @ResponseBody
    public Map<String, String> currentBoard(@PathVariable String roomName) {
        return springChessService.currentBoardByRoomName(roomName);
    }

    @GetMapping(value = "/rooms/{roomName}/turn")
    @ResponseBody
    public TurnDto currentTurn(@PathVariable String roomName) {
        return new TurnDto(springChessService.turnName(roomName));
    }

    @GetMapping(value = "/rooms/{roomName}/score")
    @ResponseBody
    public ScoreDto score(@PathVariable String roomName) {
        return springChessService.score(roomName);
    }

    @GetMapping("/{roomName}/check")
    @ResponseBody
    public RoomValidateDto checkRoomName(@PathVariable String roomName) {
        return springChessService.checkDuplicatedRoom(roomName);
    }

    @DeleteMapping("/rooms/{roomName}")
    @ResponseBody
    public void deleteRoom(@PathVariable String roomName) {
        springChessService.deleteRoom(roomName);
    }

    @ExceptionHandler(NotExistRoomException.class)
    public ResponseEntity<String> dbException(NotExistRoomException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ChessException.class)
    public ResponseEntity<String> chessException(ChessException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
