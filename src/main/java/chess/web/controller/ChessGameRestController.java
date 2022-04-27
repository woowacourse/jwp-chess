package chess.web.controller;

import chess.web.dto.MoveDto;
import chess.web.dto.MoveResultDto;
import chess.web.service.ChessGameService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessGameRestController {

    private final ChessGameService service;

    public ChessGameRestController(ChessGameService service) {
        this.service = service;
    }

    @PostMapping("/move")
    public MoveResultDto move(@RequestBody MoveDto moveDto) {
        return service.move(moveDto);
    }
}
