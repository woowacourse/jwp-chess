package chess.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chess.dto.BoardDto;
import chess.dto.MoveDto;
import chess.dto.ResultDto;
import chess.service.GameService;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/start")
    public ResponseEntity<BoardDto> start() {
        return ResponseEntity.ok().body(gameService.start());
    }

    @PostMapping("/end")
    public ResponseEntity<BoardDto> end() {
        return ResponseEntity.ok().body(gameService.end());
    }

    @GetMapping("/status")
    public ResponseEntity<ResultDto> status() {
        return ResponseEntity.ok().body(gameService.status());
    }

    @GetMapping("/load")
    public ResponseEntity<BoardDto> load() {
        return ResponseEntity.ok().body(gameService.load());
    }

    @PostMapping("/move")
    public ResponseEntity<BoardDto> move(@RequestBody MoveDto moveDto) {
        return ResponseEntity.ok().body(gameService.move(moveDto));
    }
}
