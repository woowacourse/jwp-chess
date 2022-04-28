package chess;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import chess.domain.Status;
import chess.dto.BoardDto;
import chess.dto.ExceptionResponseDto;
import chess.dto.MoveDto;
import chess.dto.RoomDto;

@Controller
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String index() {
        return "room.html";
    }

    @PostMapping("/room")
    @ResponseBody
    public ResponseEntity<Long> create(@RequestParam String name, @RequestParam String password) {
        long id = chessService.createRoom(name, password).getId();
        return ResponseEntity.created(URI.create("/room/" + id))
            .body(id);
    }

    @PostMapping("/room/{roomId}")
    @ResponseBody
    public BoardDto start(@PathVariable Long roomId) {
        return chessService.startNewGame(roomId);
    }

    @GetMapping("/room/{roomId}")
    @ResponseBody
    public BoardDto load(@PathVariable Long roomId) {
        return chessService.load(roomId);
    }

    @DeleteMapping("/room/{roomId}")
    @ResponseBody
    public boolean delete(@PathVariable Long roomId, @RequestParam String password) {
        return chessService.delete(roomId, password);
    }

    @PatchMapping("/room/{roomId}/move")
    @ResponseBody
    public BoardDto move(@PathVariable Long roomId,
        @RequestBody MoveDto moveDto) {
        return chessService.move(roomId, moveDto);
    }

    @GetMapping("/room/{roomId}/status")
    @ResponseBody
    public Status status(@PathVariable Long roomId) {
        return chessService.status(roomId);
    }

    @GetMapping("/rooms")
    @ResponseBody
    public List<RoomDto> rooms() {
        return chessService.loadRooms();
    }

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class, NoSuchElementException.class})
    public ResponseEntity<ExceptionResponseDto> handle(RuntimeException exception) {
        return ResponseEntity.badRequest()
            .body(new ExceptionResponseDto(exception.getMessage()));
    }
}
