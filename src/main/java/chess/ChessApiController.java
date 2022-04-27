package chess;

import chess.domain.Status;
import chess.dto.BoardDto;
import chess.dto.MessageDto;
import chess.dto.MoveDto;
import chess.dto.RoomCreationDto;
import chess.dto.RoomDto;
import java.util.List;
import java.util.NoSuchElementException;
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
    public RoomDto create(@RequestBody RoomCreationDto creationDto) {
        long roomId = chessService.createRoom(creationDto);
        return new RoomDto(roomId, creationDto.getName());
    }

    @GetMapping("/list")
    public List<RoomDto> list() {
        return chessService.list();
    }

    @GetMapping("/start")
    public BoardDto start(@RequestParam long roomId) {
        return chessService.startNewGame(roomId);
    }

    @GetMapping("/end")
    public MessageDto end(@RequestParam long roomId) {
        chessService.end(roomId);
        return new MessageDto("게임 종료상태로 변경했습니다.");
    }

    @GetMapping("/load")
    public BoardDto load(@RequestParam long roomId) {
        return chessService.load(roomId);
    }

    @PostMapping("/move")
    public BoardDto move(@RequestParam long roomId,
                         @RequestBody MoveDto moveDto) {
        return chessService.move(roomId, moveDto);
    }

    @GetMapping("/status")
    public Status status(@RequestParam long roomId) {
        return chessService.status(roomId);
    }

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class,
            DataIntegrityViolationException.class, NoSuchElementException.class})
    public ResponseEntity<MessageDto> handle(RuntimeException exception) {
        return ResponseEntity.badRequest()
                .body(new MessageDto(exception.getMessage()));
    }
}
