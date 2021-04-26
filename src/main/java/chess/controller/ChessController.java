package chess.controller;

import chess.dto.*;
import chess.service.ChessService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ChessController {
    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/rooms")
    public RoomsDto showRoomList() {
        return chessService.getRoomList();
    }

    @GetMapping("/board/{roomNumber}")
    public BoardDto loadSavedBoard(@PathVariable int roomNumber) {
        return chessService.getSavedBoardInfo(roomNumber);
    }

    @PostMapping("/board")
    public BoardDto makeBoard(@RequestBody Map<String, Object> data) {
        return chessService.initializeByName((String) data.get("roomName"));
    }

    @GetMapping("/reset/{roomNumber}")
    public BoardDto resetBoard(@PathVariable int roomNumber) {
        return chessService.resetBoard(roomNumber);
    }

    @GetMapping("/score/{roomNumber}")
    public ScoreDto scoreStatus(@PathVariable int roomNumber) {
        return chessService.score(roomNumber);
    }

    @PostMapping("/move/{roomNumber}")
    public ResponseDto move(@RequestBody MoveInfoDto moveInfoDto, @PathVariable int roomNumber) {
        return chessService.move(moveInfoDto, roomNumber);
    }

    @GetMapping("/move/{roomNumber}")
    public BoardDto getCurrentBoard(@PathVariable int roomNumber) {
        return chessService.getCurrentBoard(roomNumber);
    }
}
