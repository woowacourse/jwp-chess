package chess.controller.web;

import chess.controller.web.dto.ChessGameResponseDto;
import chess.controller.web.dto.RunningGameResponseDto;
import chess.domain.manager.ChessGameManagerBundle;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class RoomController {
    private final ChessService chessService;

    public RoomController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/rooms")
    public ResponseEntity<RunningGameResponseDto> getRooms() {
        ChessGameManagerBundle runningGames = chessService.findRunningGames();
        return ResponseEntity.ok(new RunningGameResponseDto(runningGames.getIdAndNextTurn()));
    }

    @PostMapping("/room")
    public ResponseEntity<ChessGameResponseDto> createRoom(@RequestBody Map<String, String> param) {
        String title = param.get("title");
        return ResponseEntity.ok(new ChessGameResponseDto(chessService.start(title)));
    }

    @DeleteMapping("room/{id:[\\d]+}")
    public ResponseEntity<Void> deleteRoom(@PathVariable long id) {
        chessService.delete(id);
        return ResponseEntity.ok().build();
    }
}
