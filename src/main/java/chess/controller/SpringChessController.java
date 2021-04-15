package chess.controller;

import chess.dto.PositionDTO;
import chess.dto.ResponseDTO;
import chess.dto.RoomNameDTO;
import chess.dto.TurnDTO;
import chess.service.SpringChessService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = "/move")
    @ResponseBody
    public ResponseDTO move(@RequestBody PositionDTO positionDTO) {
        ResponseDTO responseDTO = springChessService.move(positionDTO,"1");

        return responseDTO;
    }

    @PostMapping(value = "/restart")
    @ResponseBody
    public String restart(@RequestBody RoomNameDTO roomNameDTO) {
        springChessService.newBoard(roomNameDTO.getRoomName());
        return "성공!";
    }
}
