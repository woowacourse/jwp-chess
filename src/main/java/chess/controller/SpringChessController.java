package chess.controller;

import chess.service.SpringChessService;
import chess.webdto.ChessGameDto;
import chess.webdto.MoveRequestDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpringChessController {
    private final SpringChessService springChessService;

    public SpringChessController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    @GetMapping(value = "/games/new")
    public ChessGameDto startNewGame() {
        return springChessService.startNewGame();
    }

    @GetMapping(value = "/games/saved")
    public ChessGameDto loadSavedGame() {
        return springChessService.loadSavedGame();
    }

    @PostMapping(value = "/games/move")
    public ChessGameDto move(@RequestBody MoveRequestDto moveRequestDto) {
        final String start = moveRequestDto.getStart();
        final String destination = moveRequestDto.getDestination();
        return springChessService.move(start, destination);
    }
}
