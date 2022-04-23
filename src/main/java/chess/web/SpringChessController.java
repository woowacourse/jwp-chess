package chess.web;

import chess.service.GameService;
import chess.service.RoomService;
import chess.web.dto.CommendDto;
import chess.web.dto.RoomDto;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SpringChessController {

    private final RoomService roomService;
    private final GameService gameService;

    public SpringChessController(RoomService roomService, GameService gameService) {
        this.roomService = roomService;
        this.gameService = gameService;
    }

    @GetMapping("/")
    public String login() {
        return "login.html";
    }

    @PostMapping(value = "/board")
    public String createRoom(@RequestParam String name) {
        RoomDto roomDto = roomService.create(name);
        return "redirect:/rooms/" + roomDto.getId();
    }

    @GetMapping("/rooms/{roomId}")
    public String board(@PathVariable int roomId) {
        return "/board.html";
    }

    @GetMapping("/start")
    public ResponseEntity startNewGame(@RequestParam int roomId) {
        return ResponseEntity.ok(gameService.startNewGame(roomId));
    }

    @GetMapping("/load")
    public ResponseEntity loadGame(@RequestParam int roomId) {
        return ResponseEntity.ok(gameService.loadGame(roomId));
    }

    @PostMapping("/move")
    public ResponseEntity movePiece(@RequestParam int boardId, @RequestBody CommendDto commendDto) {
        gameService.move(boardId, commendDto);
        return ResponseEntity.ok(gameService.gameStateAndPieces(boardId));
    }

    @GetMapping("/result")
    public ResponseEntity result(@RequestParam int boardId) {
        return ResponseEntity.ok(gameService.gameResult(boardId));
    }

    @GetMapping("/end")
    public ResponseEntity end(@RequestParam int boardId) {
        return ResponseEntity.ok(gameService.gameFinalResult(boardId));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleBadRequest(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
    }
}
