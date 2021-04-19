package chess.controller;

import chess.dto.*;
import chess.service.SpringChessService;
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

    @PostMapping("/create")
    @ResponseBody
    public void createRoom(@RequestBody RoomNameDto roomNameDTO) {
        springChessService.createRoom(roomNameDTO.getRoomName());
    }

    @PostMapping(value = "/rooms/{roomName}/move")
    @ResponseBody
    public ResponseDto move(@RequestBody PositionDto positionDto, @PathVariable RoomNameDto roomName) {
        ResponseDto responseDto = springChessService.move(positionDto, roomName.getRoomName());
        return responseDto;
    }

    @PostMapping("/rooms/{roomName}/current-board")
    @ResponseBody
    public Map<String, String> currentBoard(@PathVariable RoomNameDto roomName) {
        return springChessService.currentBoardByRoomName(roomName.getRoomName());
    }

    @PostMapping(value = "/rooms/{roomName}/current-turn")
    @ResponseBody
    public TurnDto currentTurn(@PathVariable RoomNameDto roomName) {
        return new TurnDto(springChessService.turnName(roomName.getRoomName()));
    }

    @PostMapping(value = "/rooms/{roomName}/restart")
    @ResponseBody
    public void restart(@PathVariable RoomNameDto roomName) {
        springChessService.newBoard(roomName.getRoomName());
    }

    @PostMapping(value = "/rooms/{roomName}/score")
    @ResponseBody
    public ScoreDto score(@PathVariable RoomNameDto roomName) {
        return springChessService.score(roomName.getRoomName());
    }

    @PostMapping("/check-room-name")
    @ResponseBody
    public RoomValidateDto checkRoomName(@RequestBody RoomNameDto roomName) {
        return springChessService.checkDuplicatedRoom(roomName.getRoomName());
    }

    @DeleteMapping("/rooms/{roomName}")
    @ResponseBody
    public void deleteRoom(@PathVariable RoomNameDto roomName) {
        springChessService.deleteRoom(roomName.getRoomName());
    }
}
