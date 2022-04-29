package chess.controller;

import chess.dto.response.BoardResponse;
import chess.dto.request.MoveRequest;
import chess.dto.response.ResultResponse;
import chess.service.GameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class ChessGameController {

    private final GameService gameService;

    public ChessGameController(final GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/{id}/start")
    public BoardResponse start(@PathVariable("id") String id) {
        return gameService.start(id);
    }

    @PostMapping("/{id}/end")
    public BoardResponse end(@PathVariable("id") String id) {
        return gameService.end(id);
    }

    @GetMapping("/{id}/status")
    public ResultResponse status(@PathVariable("id") String id) {
        return gameService.status(id);
    }

    @GetMapping("/{id}/load")
    public BoardResponse load(@PathVariable("id") String id) {
        return gameService.load(id);
    }

    @PostMapping("/{id}/move")
    public BoardResponse move(@PathVariable("id") String id, @RequestBody MoveRequest moveRequest) {
        return gameService.move(id, moveRequest);
    }
}
