package chess.controller;

import chess.dto.RoomExistResponseDto;
import chess.service.GameService;
import chess.service.PieceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/games")
@RestController
public class GameController {

    private final GameService gameService;
    private final PieceService pieceService;

    public GameController(GameService gameService, PieceService pieceService) {
        this.gameService = gameService;
        this.pieceService = pieceService;
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Integer> deleteGameRoom(@PathVariable long roomId) {
        pieceService.removeAll(roomId);
        return ResponseEntity.ok(gameService.deleteByGameId(roomId));
    }

    @GetMapping("/rooms/check")
    public ResponseEntity<RoomExistResponseDto> findGameByName(@RequestParam String name) {
        return ResponseEntity.ok(gameService.findGameByName(name));
    }

}
