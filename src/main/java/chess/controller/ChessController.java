package chess.controller;

import chess.dto.*;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ChessController {
    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/rooms")
    public ResponseEntity<RoomsDto> showRoomList() {
        RoomsDto roomsDto = chessService.getRoomList();
        return ResponseEntity.ok().body(roomsDto);
    }

    @GetMapping("/board/{roomNumber}")

    public ResponseEntity<BoardDto> loadSavedBoard(@PathVariable int roomNumber) {
        BoardDto boardDto = chessService.getSavedBoardInfo(roomNumber);
        return ResponseEntity.ok().body(boardDto);
    }

    @PostMapping("/board")
    public ResponseEntity<BoardDto> makeBoard(@RequestBody Map<String, Object> data) {
        BoardDto boardDto = chessService.initializeByName((String) data.get("roomName"));
        return ResponseEntity.ok().body(boardDto);
    }

    @GetMapping("/reset/{roomNumber}")
    public ResponseEntity<BoardDto> resetBoard(@PathVariable int roomNumber) {
        BoardDto boardDto = chessService.resetBoard(roomNumber);
        return ResponseEntity.ok().body(boardDto);
    }

    @GetMapping("/score/{roomNumber}")
    public ResponseEntity<ScoreDto> scoreStatus(@PathVariable int roomNumber) {
        ScoreDto scoreDto = chessService.score(roomNumber);
        return ResponseEntity.ok().body(scoreDto);
    }

    @PostMapping("/move/{roomNumber}")
    public ResponseEntity<BoardDto> move(@RequestBody MoveInfoDto moveInfoDto, @PathVariable int roomNumber) {
        BoardDto boardDto = chessService.move(moveInfoDto, roomNumber);
        return ResponseEntity.ok().body(boardDto);
    }
}
