package chess.controller.web;

import chess.chessgame.domain.room.Room;
import chess.chessgame.domain.room.game.ChessGameManagerBundle;
import chess.controller.web.dto.RunningGameResponseDto;
import chess.service.ChessService;
import chess.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoomController {
    private final ChessService chessService;
    private final RoomService roomService;

    public RoomController(ChessService chessService, RoomService roomService) {
        this.chessService = chessService;
        this.roomService = roomService;
    }

    @GetMapping("/room")
    public ResponseEntity<RunningGameResponseDto> entranceRoom() {
        ChessGameManagerBundle runningGames = chessService.findRunningGames();
        return ResponseEntity.ok(new RunningGameResponseDto(runningGames.getIdAndNextTurn()));
    }

    @PostMapping("/room")
    public ResponseEntity createRoom() {
        roomService.createRoom();
        return null;
    }
}
