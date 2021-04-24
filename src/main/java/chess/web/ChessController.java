package chess.web;

import chess.service.ChessService;
import chess.web.dto.GameDto;
import chess.web.dto.MessageDto;
import chess.web.dto.MoveDto;
import chess.web.dto.StatusDto;
import org.springframework.web.bind.annotation.*;

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
