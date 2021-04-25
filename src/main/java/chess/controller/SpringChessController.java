package chess.controller;

import chess.dto.*;
import chess.exception.ChessException;
import chess.exception.DuplicatedRoomNameException;
import chess.exception.NotExistRoomException;
import chess.service.SpringChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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

    @GetMapping("/enter/{roomName}")
    public String enterRoom(@PathVariable String roomName, HttpSession httpSession) {
        httpSession.setAttribute("roomName", roomName);
        return "/chess.html";
    }

    @GetMapping("/currentRoom")
    public ResponseEntity<RoomNameDTO> currentRoom(@SessionAttribute String roomName) {
        return ResponseEntity.ok().body(new RoomNameDTO(roomName));
    }

    @PostMapping(value = "/restart")
    public ResponseEntity<Void> restart(@SessionAttribute String roomName) {
        springChessService.newBoard(roomName);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/move")
    @ResponseBody
    public ResponseEntity<ResponseDTO> move(@RequestBody PositionDTO positionDTO, @SessionAttribute String roomName) {
        return ResponseEntity.ok().body(springChessService.move(positionDTO, roomName));
    }

    @GetMapping("/currentBoard")
    public ResponseEntity<Map<String, String>> currentBoard(@SessionAttribute String roomName) {
        return ResponseEntity.ok().body(springChessService.currentBoardByRoomName(roomName));
    }

    @PostMapping(value = "/currentTurn")
    public ResponseEntity<TurnDTO> currentTurn(@SessionAttribute String roomName) {
        return ResponseEntity.ok().body(new TurnDTO(springChessService.turnName(roomName)));
    }

    @PostMapping(value = "/score")
    public ResponseEntity<ScoreDTO> score(@SessionAttribute String roomName) {
        return ResponseEntity.ok().body(springChessService.score(roomName));
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<String>> roomNames() {
        return ResponseEntity.ok().body(springChessService.rooms());
    }

    @PostMapping("/checkRoomName")
    @ResponseBody
    public ResponseEntity<RoomValidateDTO> checkRoomName(@RequestBody RoomNameDTO roomNameDTO) {
        return ResponseEntity.ok().body(springChessService.checkDuplicatedRoom(roomNameDTO.getRoomName()));
    }

    @PostMapping("/createRoom")
    @ResponseBody
    public void createRoom(@RequestBody RoomNameDTO roomNameDTO) {
        springChessService.createRoom(roomNameDTO.getRoomName());
    }

    @DeleteMapping("/deleteRoom")
    @ResponseBody
    public void deleteRoom(@RequestBody RoomNameDTO roomNameDTO) {
        springChessService.deleteRoom(roomNameDTO.getRoomName());
    }
}
