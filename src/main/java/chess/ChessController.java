package chess;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import chess.domain.Status;
import chess.dto.BoardDto;
import chess.dto.ExceptionResponseDto;
import chess.dto.MoveDto;

@Controller
public class ChessController {

    private final ChessService chessService;

    public ChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String index() {
        return "lobby.html";
    }

    @GetMapping("/room")
    public String room(@RequestParam String name,
        Model model) {
        model.addAttribute("name", name);
        return "room.html";
    }

    @PostMapping("/room")
    @ResponseBody
    public ResponseEntity<BoardDto> start(@RequestParam String name) {
        chessService.createRoom(name);
        return ResponseEntity.created(URI.create("/room/" + name))
            .body(chessService.startNewGame(name));
    }

    @GetMapping("/room/{roomName}")
    @ResponseBody
    public BoardDto load(@PathVariable String roomName) {
        return chessService.load(roomName);
    }

    @PutMapping("/room/{roomName}/move")
    @ResponseBody
    public BoardDto move(@PathVariable String roomName,
        @RequestBody MoveDto moveDto) {
        return chessService.move(roomName, moveDto);
    }

    @GetMapping("/room/{roomName}/status")
    @ResponseBody
    public Status status(@PathVariable String roomName) {
        return chessService.status(roomName);
    }

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    public ResponseEntity<ExceptionResponseDto> handle(RuntimeException exception) {
        return ResponseEntity.badRequest()
            .body(new ExceptionResponseDto(exception.getMessage()));
    }
}
