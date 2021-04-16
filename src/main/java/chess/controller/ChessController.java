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

    @GetMapping("/board")
    public BoardDto loadSavedBoard() {
        return chessService.getSavedBoard();
    }

    @PutMapping("/board")
    public BoardDto resetBoard() {
        return chessService.resetBoard();
    }

    @GetMapping("/score")
    public ScoreDto scoreStatus() {
        return ScoreDto.of(chessService.score());
    }

    @PostMapping("/move")
    public BoardDto move(@RequestBody MoveInfoDto moveInfoDto) {
        return chessService.move(moveInfoDto);
    }
}
