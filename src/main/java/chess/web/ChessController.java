package chess.web;

import chess.exception.ChessGameException;
import chess.service.ChessService;
import chess.web.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.util.List;

@RestController
@RequestMapping("/game")
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/{roomId}")
    public GameDto load(@PathVariable Long roomId) {
        return chessService.loadByGameId(roomId);
    }

    @PatchMapping("/{roomId}/position")
    public GameDto move(@PathVariable Long roomId, @RequestBody MoveDto moveDto) {
        String source = moveDto.getSource();
        String target = moveDto.getTarget();

        return chessService.move(roomId, source, target);
    }

    @GetMapping("/{roomId}/status")
    public StatusDto status(@PathVariable Long roomId) {
        return chessService.getStatus(roomId);
    }

    @PatchMapping("/{roomId}/end")
    public MessageDto end(@PathVariable Long roomId) {
        return chessService.end(roomId);
    }

}
