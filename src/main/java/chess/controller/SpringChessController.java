package chess.controller;

import chess.dto.PositionDTO;
import chess.dto.RoomNameDTO;
import chess.dto.TurnDTO;
import chess.exception.ChessException;
import chess.exception.NotExistRoomException;
import chess.service.SpringChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

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
    public ResponseEntity currentRoom(@SessionAttribute String roomName) {
        return ResponseEntity.ok().body(new RoomNameDTO(roomName));
    }

    @PostMapping(value = "/restart")
    public void restart(@SessionAttribute String roomName) {
        springChessService.newBoard(roomName);
    }

    @PostMapping(value = "/move")
    @ResponseBody
    public ResponseEntity move(@RequestBody PositionDTO positionDTO, @SessionAttribute String roomName) {
        return ResponseEntity.ok().body(springChessService.move(positionDTO, roomName));
    }

    @GetMapping("/currentBoard")
    public ResponseEntity currentBoard(@SessionAttribute String roomName) {
        return ResponseEntity.ok().body(springChessService.currentBoardByRoomName(roomName));
    }

    @PostMapping(value = "/currentTurn")
    public ResponseEntity currentTurn(@SessionAttribute String roomName) {
        return ResponseEntity.ok().body(new TurnDTO(springChessService.turnName(roomName)));
    }

    @PostMapping(value = "/score")
    public ResponseEntity score(@SessionAttribute String roomName) {
        return ResponseEntity.ok().body(springChessService.score(roomName));
    }

    @GetMapping("/rooms")
    public ResponseEntity roomNames() {
        return ResponseEntity.ok().body(springChessService.rooms());
    }

    @PostMapping("/checkRoomName")
    @ResponseBody
    public ResponseEntity checkRoomName(@RequestBody RoomNameDTO roomNameDTO) {
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

    @ExceptionHandler(NotExistRoomException.class)
    public ResponseEntity dbException(NotExistRoomException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ChessException.class)
    public ResponseEntity chessException(ChessException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
