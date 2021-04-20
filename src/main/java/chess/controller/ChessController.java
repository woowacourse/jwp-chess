package chess.controller;

import chess.domain.dto.BoardDto;
import chess.domain.dto.MoveInfoDto;
import chess.domain.dto.ScoreDto;
import chess.service.ChessService;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChessController {
    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/board/{roomName}")
    public BoardDto loadSavedBoard(@PathVariable String roomName) {
        return chessService.getSavedBoard(roomName);
    }

    @PutMapping("/board/{roomName}")
    public BoardDto resetBoard(@PathVariable String roomName) {
        return chessService.resetBoard(roomName);
    }

    @GetMapping("/score/{roomName}")
    public ScoreDto scoreStatus(@PathVariable String roomName) {
        return chessService.score(roomName);
    }

    @PostMapping("/move/{roomName}")
    public BoardDto move(@RequestBody MoveInfoDto moveInfoDto, @PathVariable String roomName) {
        return chessService.move(moveInfoDto, roomName);
    }
}
