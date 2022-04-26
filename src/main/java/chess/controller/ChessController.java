package chess.controller;

import chess.dto.BoardDto;
import chess.dto.GameStateDto;
import chess.dto.MoveDto;
import chess.dto.ScoreDto;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessController {
    private static final int ROOM_ID = 1;
    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/board")
    public ResponseEntity<BoardDto> getBoard() {
        return ResponseEntity.ok().body(chessService.getInitialBoard(ROOM_ID));
    }

    @PostMapping(value = "/move")
    public ResponseEntity<GameStateDto> move(@RequestBody MoveDto moveDto) {
        return ResponseEntity.ok().body(chessService.move(moveDto, ROOM_ID));
    }

    @GetMapping("/status")
    public ResponseEntity<ScoreDto> getStatus() {
        return ResponseEntity.ok().body(chessService.getStatus(ROOM_ID));
    }

    @PostMapping("/reset")
    public ResponseEntity<BoardDto> reset() {
        return ResponseEntity.ok().body(chessService.resetBoard(ROOM_ID));
    }

    @PostMapping("/end")
    public ResponseEntity<GameStateDto> end() {
        return ResponseEntity.ok().body(chessService.end(ROOM_ID));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> IllegalArgumentExceptionHandle(RuntimeException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> ExceptionHandle() {
        return ResponseEntity.badRequest().body("실행 중 예상치 못한 오류가 발생했습니다.");
    }
}
