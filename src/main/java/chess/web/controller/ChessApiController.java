package chess.web.controller;

import chess.board.Board;
import chess.web.service.ChessService;
import chess.web.service.dto.BoardDto;
import chess.web.service.dto.MoveDto;
import chess.web.service.dto.ScoreDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
public class ChessApiController {

    private final ChessService chessService;

    public ChessApiController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/load")
    public ResponseEntity<BoardDto> loadGame() {
        Board board = chessService.loadGame(1L);
        return ResponseEntity.ok().body(BoardDto.from(board));
    }

    @GetMapping("/restart")
    public ResponseEntity<BoardDto> initBoard() {
        Board board = chessService.initBoard(1L);
        return ResponseEntity.ok().body(BoardDto.from(board));
    }

    @GetMapping("/status")
    public ResponseEntity<ScoreDto> getStatus() {
        ScoreDto status = chessService.getStatus(1L);
        return ResponseEntity.ok().body(status);
    }

    @PostMapping("/move")
    public ResponseEntity<BoardDto> move(@RequestBody MoveDto moveDto) {
        Board board = chessService.move(moveDto, 1L);
        return ResponseEntity.ok().body(BoardDto.from(board));
    }
}
