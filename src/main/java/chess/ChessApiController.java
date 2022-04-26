package chess;

import chess.domain.Status;
import chess.dto.BoardDto;
import chess.dto.ExceptionResponseDto;
import chess.dto.MoveDto;
import chess.dto.RoomCreationDto;
import chess.dto.RoomIdDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessApiController {

    private final ChessService chessService;

    public ChessApiController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public RoomIdDto create(@RequestBody RoomCreationDto creationDto) {
        long roomId = chessService.createRoom(creationDto);
        return new RoomIdDto(roomId);
    }

    @GetMapping("/start")
    public BoardDto start(@RequestParam String name) {
        return chessService.startNewGame(name);
    }

    @GetMapping("/load")
    public BoardDto load(@RequestParam String name) {
        return chessService.load(name);
    }

    @PostMapping("/move")
    public BoardDto move(@RequestParam String name,
                         @RequestBody MoveDto moveDto) {
        return chessService.move(name, moveDto);
    }

    @GetMapping("/status")
    public Status status(@RequestParam String name) {
        return chessService.status(name);
    }

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class,
            DataIntegrityViolationException.class})
    public ResponseEntity<ExceptionResponseDto> handle(RuntimeException exception) {
        return ResponseEntity.badRequest()
                .body(new ExceptionResponseDto(exception.getMessage()));
    }
}
