package chess.web;

import chess.exception.ChessGameException;
import chess.service.ChessService;
import chess.web.dto.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.util.List;

@RestController
@RequestMapping("/games")
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping(value = "/{roomId}")
    public GameDto load(@PathVariable Long roomId) {
        return chessService.loadByGameId(roomId);
    }

    @PatchMapping(value = "/{roomId}/position")
    public GameDto move(@PathVariable Long roomId, @RequestBody MoveDto moveDto) {
        String source = moveDto.getSource();
        String target = moveDto.getTarget();

        return chessService.move(roomId, source, target);
    }

    @GetMapping(value = "/{roomId}/status")
    public StatusDto status(@PathVariable Long roomId) {
        return chessService.getStatus(roomId);
    }

    @PatchMapping(value = "/{roomId}/end")
    public MessageDto end(@PathVariable Long roomId) {
        return chessService.end(roomId);
    }

}
