package chess.controller;

import chess.domain.position.Position;
import chess.dto.GameStatusDto;
import chess.dto.MoveForm;
import chess.dto.StatusDto;
import chess.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RestChessController {

    private final GameService gameService;

    public RestChessController(GameService gameService) {
        this.gameService = gameService;
    }

    @PatchMapping("/room/{roomId}/move")
    public ResponseEntity<GameStatusDto> moveByCommand(@PathVariable("roomId") int id, @RequestBody MoveForm moveForm) {
        gameService.move(id, Position.of(moveForm.getSource()), Position.of(moveForm.getTarget()));
        return new ResponseEntity<>(new GameStatusDto(gameService.isEnd(id)), HttpStatus.OK);
    }

    @GetMapping("/room/{roomId}/status")
    public StatusDto showStatus(@PathVariable("roomId") int id) {
        return gameService.status(id);
    }
}
