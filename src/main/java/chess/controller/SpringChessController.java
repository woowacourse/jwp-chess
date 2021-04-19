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

    @GetMapping("/{roomName}/enter")
    public String enterRoom() {
        return "/chess.html";
    }

    @PostMapping(value = "/restart")
    @ResponseBody
    public void restart(@RequestBody RoomNameDTO roomNameDTO) {
        springChessService.newBoard(roomNameDTO.getRoomName());
    }

    @PostMapping(value = "/move")
    @ResponseBody
    public ResponseDTO move(@RequestBody PositionDTO positionDTO) {
        ResponseDTO responseDTO = springChessService.move(positionDTO, positionDTO.roomName());
        return responseDTO;
    }

    @PostMapping("/currentBoard")
    @ResponseBody
    public Map<String, String> currentBoard(@RequestBody RoomNameDTO roomNameDTO) {
        return springChessService.currentBoardByRoomName(roomNameDTO.getRoomName());
    }

    @PostMapping(value = "/currentTurn")
    @ResponseBody
    public TurnDTO currentTurn(@RequestBody RoomNameDTO roomNameDTO) {
        return new TurnDTO(springChessService.turnName(roomNameDTO.getRoomName()));
    }

    @PostMapping(value = "/score")
    @ResponseBody
    public ScoreDTO score(@RequestBody RoomNameDTO roomNameDTO) {
        return springChessService.score(roomNameDTO.getRoomName());
    }

    @GetMapping("/rooms")
    @ResponseBody
    public List<String> roomNames() {
        return springChessService.rooms();
    }

    @PostMapping("/checkRoomName")
    @ResponseBody
    public RoomValidateDTO checkRoomName(@RequestBody RoomNameDTO roomNameDTO) {
        return springChessService.checkDuplicatedRoom(roomNameDTO.getRoomName());
    }

    @PostMapping("/createRoom")
    @ResponseBody
    public void createRoom(@RequestBody RoomNameDTO roomNameDTO) {
        springChessService.createRoom(roomNameDTO.getRoomName());
    }

    @PostMapping("/deleteRoom")
    @ResponseBody
    public void deleteRoom(@RequestBody RoomNameDTO roomNameDTO) {
        springChessService.deleteRoom(roomNameDTO.getRoomName());
    }
}
