package chess.controller;

import chess.domain.Status;
import chess.dto.BoardDto;
import chess.dto.MessageDto;
import chess.dto.MoveDto;
import chess.dto.PasswordDto;
import chess.dto.RoomCreationDto;
import chess.dto.RoomDto;
import chess.service.ChessService;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
public class ChessApiController {

    private static final String UPDATE_RESULT_MESSAGE = "게임 종료상태로 변경했습니다.";
    private static final String DELETE_RESULT_MESSAGE = "방을 삭제했습니다.";
    private final ChessService chessService;

    public ChessApiController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public RoomDto create(@RequestBody RoomCreationDto creationDto) {
        long roomId = chessService.createRoom(creationDto);
        return new RoomDto(roomId, creationDto.getName());
    }

    @GetMapping()
    public List<RoomDto> list() {
        return chessService.list();
    }

    @GetMapping("/{roomId}/start")
    public BoardDto start(@PathVariable long roomId) {
        return chessService.startNewGame(roomId);
    }

    @PatchMapping("/{roomId}/end")
    public MessageDto end(@PathVariable long roomId) {
        chessService.end(roomId);
        return new MessageDto(UPDATE_RESULT_MESSAGE);
    }

    @GetMapping("/{roomId}/load")
    public BoardDto load(@PathVariable long roomId) {
        return chessService.load(roomId);
    }

    @PatchMapping("/{roomId}/board")
    public BoardDto move(@PathVariable long roomId,
                         @RequestBody MoveDto moveDto) {
        return chessService.move(roomId, moveDto);
    }

    @GetMapping("/{roomId}/status")
    public Status status(@PathVariable long roomId) {
        return chessService.status(roomId);
    }

    @DeleteMapping("/{roomId}")
    public MessageDto delete(@PathVariable long roomId,
                             @RequestBody PasswordDto passwordDto) {
        chessService.delete(roomId, passwordDto);
        return new MessageDto(DELETE_RESULT_MESSAGE);
    }

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class,
            DataIntegrityViolationException.class, NoSuchElementException.class})
    public ResponseEntity<MessageDto> handle(RuntimeException exception) {
        return ResponseEntity.badRequest()
                .body(new MessageDto(exception.getMessage()));
    }
}
