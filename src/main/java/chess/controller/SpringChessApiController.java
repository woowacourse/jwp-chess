package chess.controller;

import chess.domain.exceptions.DatabaseException;
import chess.dto.GameResponseDto;
import chess.dto.MovedInfoDto;
import chess.dto.StatusDto;
import chess.service.ChessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SpringChessApiController {

    private final ChessService chessService;

    public SpringChessApiController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("/game")
    public GameResponseDto game(@RequestParam String roomName) {
        return GameResponseDto.of(chessService.currentGame(roomName));
    }

    @GetMapping("/restart")
    public GameResponseDto restart(@RequestParam String roomName) {
        return GameResponseDto.of(chessService.restartGame(roomName));
    }

    @PostMapping("/move")
    public MovedInfoDto move(@RequestParam String roomName, @RequestParam String source, @RequestParam String target) {
        return chessService.move(roomName, source, target);
    }

    @GetMapping("/status")
    public StatusDto status(@RequestParam String roomName) {
        return new StatusDto(chessService.currentGame(roomName));
    }

    @PostMapping("/end")
    public void end(@RequestParam String roomName) {
        chessService.savePlayingBoard(roomName);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> error(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({DatabaseException.class})
    public ResponseEntity<String> error(DatabaseException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
