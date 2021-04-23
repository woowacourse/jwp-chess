package chess.web;

import chess.exception.ChessGameException;
import chess.service.ChessService;
import chess.web.dto.GameDto;
import chess.web.dto.MessageDto;
import chess.web.dto.MoveDto;
import chess.web.dto.StatusDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SpringWebChessController {

    private final ChessService chessService;

    public SpringWebChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/game/{gameId}/start")
    public GameDto start(@PathVariable String gameId) {
        return chessService.startNewGame(gameId);
    }

    @GetMapping("/game/{gameId}")
    public GameDto load(@PathVariable String gameId) {
        return chessService.loadByGameId(gameId);
    }

    @PatchMapping("/game/{gameId}/position")
    public GameDto move(@PathVariable String gameId, @RequestBody MoveDto moveDto) {
        String source = moveDto.getSource();
        String target = moveDto.getTarget();

        return chessService.move(gameId, source, target);
    }

    @GetMapping("/game/{gameId}/status")
    public StatusDto status(@PathVariable String gameId) {
        return chessService.getStatus(gameId);
    }

    @PatchMapping("/game/{gameId}/end")
    public MessageDto end(@PathVariable String gameId) {
        return chessService.end(gameId);
    }

    @ExceptionHandler
    public ResponseEntity<MessageDto> handle(ChessGameException e) {
        return ResponseEntity.badRequest().body(new MessageDto(e.getMessage()));
    }

}
