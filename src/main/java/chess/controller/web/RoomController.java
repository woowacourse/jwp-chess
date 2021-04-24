package chess.controller.web;

import chess.chessgame.domain.room.game.ChessGameManagerBundle;
import chess.controller.web.dto.RunningGameResponseDto;
import chess.service.ChessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoomController {
    private final ChessService chessService;

    public RoomController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/room")
    public ResponseEntity<RunningGameResponseDto> getGames() {
        ChessGameManagerBundle runningGames = chessService.findRunningGames();
        return ResponseEntity.ok(new RunningGameResponseDto(runningGames.getIdAndNextTurn()));
    }
}
