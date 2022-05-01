package chess;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import chess.domain.Status;
import chess.dto.BoardDto;
import chess.dto.ExceptionResponseDto;
import chess.dto.MoveDto;
import chess.dto.RoomDto;

@RestController
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/rooms")
    public List<RoomDto> findAllRooms() {
        return chessService.findAllRooms();
    }

    @PostMapping("/rooms")
    public ResponseEntity<Long> create(@RequestParam String name, @RequestParam String password) {
        long id = chessService.createRoom(name, password).getId();
        return ResponseEntity.created(URI.create("/rooms/" + id))
            .body(id);
    }

    @PostMapping("/rooms/{id}")
    public BoardDto start(@PathVariable long id) {
        return chessService.startNewGame(id);
    }

    @GetMapping("/rooms/{id}")
    public BoardDto findRoom(@PathVariable long id) {
        return chessService.findRoom(id);
    }

    @DeleteMapping("/rooms/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id, @RequestParam String password) {
        chessService.delete(id, password);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/rooms/{id}/move")
    public BoardDto move(@PathVariable long id,
        @RequestBody MoveDto moveDto) {
        return chessService.move(id, moveDto);
    }

    @GetMapping("/rooms/{id}/status")
    public Status status(@PathVariable long id) {
        return chessService.status(id);
    }

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class, NoSuchElementException.class})
    public ResponseEntity<ExceptionResponseDto> handle(RuntimeException exception) {
        return ResponseEntity.badRequest()
            .body(new ExceptionResponseDto(exception.getMessage()));
    }
}
