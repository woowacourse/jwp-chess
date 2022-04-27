package chess.controller;

import chess.dto.BoardDto;
import chess.dto.MoveDto;
import chess.dto.ResultDto;
import chess.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class ChessGameController {

    private final GameService gameService;

    public ChessGameController(final GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/{id}/start")
    public BoardDto start(@PathVariable("id") String id) {
        return gameService.start(id);
    }

    @PostMapping("/{id}/end")
    public BoardDto end(@PathVariable("id") String id) {
        return gameService.end(id);
    }

    @GetMapping("/{id}/status")
    public ResultDto status(@PathVariable("id") String id) {
        return gameService.status(id);
    }

    @GetMapping("/{id}/load")
    public BoardDto load(@PathVariable("id") String id) {
        return gameService.load(id);
    }

    @PostMapping("/{id}/move")
    public BoardDto move(@PathVariable("id") String id, @RequestBody MoveDto moveDto) {
        return gameService.move(id, moveDto);
    }
}
