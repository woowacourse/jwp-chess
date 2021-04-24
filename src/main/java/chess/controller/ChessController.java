package chess.controller;

import chess.domain.dto.*;
import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class ChessController {
    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String start() {
        return "main.html";
    }

    @GetMapping("/rooms")
    @ResponseBody
    public RoomsDto showRoomList() {
        return chessService.getRoomList();
    }

    @GetMapping("/rooms/{roomNumber}")
    public String moveRoom() {
        return "/chess.html";
    }

    @GetMapping("/board/{roomNumber}")
    @ResponseBody
    public BoardDto loadSavedBoard(@PathVariable int roomNumber) {
        return chessService.getSavedBoardInfo(roomNumber);
    }

    @PostMapping("/board")
    @ResponseBody
    public BoardDto makeBoard(@RequestBody Map<String, Object> data) {
        return chessService.initializeByName((String) data.get("roomName"));
    }

    @GetMapping("/reset/{roomNumber}")
    @ResponseBody
    public BoardDto resetBoard(@PathVariable int roomNumber) {
        return chessService.resetBoard(roomNumber);
    }

    @GetMapping("/score/{roomNumber}")
    @ResponseBody
    public ScoreDto scoreStatus(@PathVariable int roomNumber) {
        return chessService.score(roomNumber);
    }

    @PostMapping("/move/{roomNumber}")
    @ResponseBody
    public ResponseDto move(@RequestBody MoveInfoDto moveInfoDto, @PathVariable int roomNumber) {
        return chessService.move(moveInfoDto, roomNumber);
    }

    @GetMapping("/move/{roomNumber}")
    @ResponseBody
    public BoardDto getCurrentBoard(@PathVariable int roomNumber) {
        return chessService.getCurrentBoard(roomNumber);
    }
}
