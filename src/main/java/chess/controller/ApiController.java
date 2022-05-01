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
public class ApiController {

    private final GameService gameService;

    public ApiController(GameService gameService) {
        this.gameService = gameService;
    }

    @PatchMapping("/room/{roomId}/move")
    public ResponseEntity<GameStatusDto> moveByCommand(@PathVariable("roomId") int id, @RequestBody MoveForm moveForm) {
        if (gameService.isEnd(id)) {
            throw new IllegalArgumentException("게임이 종료되어 기물을 움직일 수 없습니다.");
        }
        gameService.move(id, Position.of(moveForm.getSource()), Position.of(moveForm.getTarget()));
        return new ResponseEntity<>(new GameStatusDto(gameService.isEnd(id)), HttpStatus.OK);
    }

    @GetMapping("/room/{roomId}/status")
    public StatusDto showStatus(@PathVariable("roomId") int id) {
        return gameService.status(id);
    }

    @PostMapping("/room/{roomId}/end")
    public ResponseEntity<Void> endGame(@PathVariable("roomId") int id, @RequestParam("password") String password) {
        if (!gameService.isEnd(id)) {
            throw new IllegalArgumentException("진행중인 게임은 삭제할 수 없습니다.");
        }
        if (!gameService.end(id, password)) {
            throw new IllegalArgumentException("게임 삭제에 실패하였습니다.");
        }
        return ResponseEntity.ok(null);
    }
}
