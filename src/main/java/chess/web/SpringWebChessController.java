package chess.web;

import chess.exception.ChessGameException;
import chess.service.ChessService;
import chess.web.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.util.List;

@RestController
public class SpringWebChessController {

    private final ChessService chessService;

    public SpringWebChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("/room/{roomName}")
    public RoomDto createNewRoom(@PathVariable String roomName) {
        return chessService.createNewRoom(roomName);
    }

    @GetMapping("/game/{roomId}")
    public GameDto load(@PathVariable Long roomId) {
        return chessService.loadByGameId(roomId);
    }

    @GetMapping("/rooms")
    public List<RoomDto> rooms() {
        return chessService.getAllRooms();
    }

    @PatchMapping("/game/{roomId}/position")
    public GameDto move(@PathVariable Long roomId, @RequestBody MoveDto moveDto) {
        String source = moveDto.getSource();
        String target = moveDto.getTarget();

        return chessService.move(roomId, source, target);
    }

    @GetMapping("/game/{roomId}/status")
    public StatusDto status(@PathVariable Long roomId) {
        return chessService.getStatus(roomId);
    }

    @PatchMapping("/game/{roomId}/end")
    public MessageDto end(@PathVariable Long roomId) {
        return chessService.end(roomId);
    }

    @ExceptionHandler
    public ResponseEntity<MessageDto> handle(ChessGameException e) {
        return ResponseEntity.badRequest().body(new MessageDto(e.getMessage()));
    }

}
