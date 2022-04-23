package chess.controller;

import chess.dto.ErrorMessageDto;
import chess.dto.MoveDto;
import chess.dto.ResultDto;
import chess.dto.ScoreDto;
import chess.service.SpringChessService;
import chess.view.ChessMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SpringChessGameController {

    private final SpringChessService springChessService;

    public SpringChessGameController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    @GetMapping("/start")
    public ResponseEntity<ChessMap> start() {
        return ResponseEntity.ok(springChessService.initializeGame());
    }

    @GetMapping("/load")
    public ResponseEntity<ChessMap> load() {
        return ResponseEntity.ok(springChessService.load());
    }

    @GetMapping("/status")
    public ResponseEntity<ScoreDto> status() {
        return ResponseEntity.ok(springChessService.getStatus());
    }

    @PostMapping("/move")
    public ResponseEntity<ChessMap> move(@RequestBody MoveDto moveDto) {
        return ResponseEntity.ok(springChessService.move(moveDto));
    }

    @GetMapping("/end")
    public ResponseEntity<ResultDto> end() {
        final ResultDto resultDto = springChessService.getResult();
        springChessService.initializeGame();
        return ResponseEntity.ok(resultDto);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessageDto> handle(Exception e) {
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(e.getMessage());
        return ResponseEntity.badRequest().body(errorMessageDto);
    }
}
