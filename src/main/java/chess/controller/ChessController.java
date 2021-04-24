package chess.controller;

import chess.domain.ChessGame;
import chess.domain.dto.*;
import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class ChessController {
    private final ChessService chessService;
    private final ChessGame chessGame;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
        this.chessGame = new ChessGame();
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
        return chessService.getSavedBoardInfo(chessGame, roomNumber);
    }

    @PostMapping("/board")
    @ResponseBody
    public BoardDto resetBoard(@RequestBody Map<String, Object> data) {
        return chessService.initialize(chessGame, (String)data.get("roomName"));
    }

//    @PutMapping("/board/{roomNumber}")
//    @ResponseBody
//    public BoardDto resetBoard(@RequestParam String roomName) {
//        return chessService.initialize(chessGame, roomName);
//    }

    @GetMapping("/score/{roomNumber}")
    @ResponseBody
    public ScoreDto scoreStatus(@PathVariable int roomNumber) {
        return chessGame.scoreStatus(roomNumber);
    }

    @PostMapping("/move/{roomNumber}")
    @ResponseBody
    public ResponseDto move(@RequestBody MoveInfoDto moveInfoDto, @PathVariable int roomNumber) {
        return chessService.move(chessGame, moveInfoDto, roomNumber);
    }

    @GetMapping("/move/{roomNumber}")
    @ResponseBody
    public BoardDto getCurrentBoard(@PathVariable int roomNumber) {
        return chessService.getCurrentBoard(chessGame, roomNumber);
    }
}
