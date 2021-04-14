package chess.web;

import chess.service.*;
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

    @GetMapping("/{gameId}/start")
    public GameDto start(@PathVariable String gameId) {
        return chessService.startNewGame(gameId);
    }

    @GetMapping("/{gameId}/load")
    public GameDto load(@PathVariable String gameId) {
        return chessService.loadByGameId(gameId);
    }

    @PatchMapping("/{gameId}/move")
    public GameDto move(@PathVariable String gameId, @RequestBody MoveDto moveDto) {
        String source = moveDto.getSource();
        String target = moveDto.getTarget();

        return chessService.move(gameId, source, target);
    }

    @PostMapping("/{gameId}/save")
    public MessageDto save(@PathVariable String gameId) {
        return chessService.save(gameId);
    }

    @GetMapping("/{gameId}/status")
    public StatusDto status(@PathVariable String gameId) {
        return chessService.getStatus(gameId);
    }

    @PatchMapping("/{gameId}/status")
    public MessageDto end(@PathVariable String gameId) {
        return chessService.end(gameId);
    }

    @ExceptionHandler
    public ResponseEntity<MessageDto> handle(RuntimeException e) {
        return ResponseEntity.badRequest().body(new MessageDto(e.getMessage()));
    }

}
