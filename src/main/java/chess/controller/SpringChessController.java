package chess.controller;

import chess.service.SpringChessService;
import chess.webdto.ChessGameDto;
import chess.webdto.MoveRequestDto;
import org.springframework.http.MediaType;
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

    @GetMapping(value = "/game/new", produces = MediaType.APPLICATION_JSON_VALUE)
    public ChessGameDto startNewGame() {
        return springChessService.startNewGame();
    }

    @GetMapping(value = "/game/saved", produces = MediaType.APPLICATION_JSON_VALUE)
    public ChessGameDto loadSavedGame() {
        return springChessService.loadSavedGame();
    }

    @PostMapping(value = "/game/move", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ChessGameDto move(@RequestBody MoveRequestDto moveRequestDto) {
        final String start = moveRequestDto.getStart();
        final String destination = moveRequestDto.getDestination();
        return springChessService.move(start, destination);
    }
}
