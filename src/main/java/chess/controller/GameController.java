package chess.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @PostMapping("/{id}/start")
    public BoardDto start(@PathVariable Long id) {
        return gameService.start(id);
    }

    @PostMapping("/{id}/end")
    public BoardDto end(@PathVariable Long id) {
        return gameService.end(id);
    }

    @GetMapping("/{id}/status")
    public ResultDto status(@PathVariable Long id) {
        return gameService.status(id);
    }

    @GetMapping("/{id}/load")
    public BoardDto load(@PathVariable Long id) {
        return gameService.load(id);
    }

    @PostMapping("/{id}/move")
    public BoardDto move(@PathVariable Long id,
        @RequestBody MoveDto moveDto) {
        return gameService.move(id, moveDto);
    }
}
