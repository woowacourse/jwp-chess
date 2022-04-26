package chess.web;

import java.util.List;

import chess.service.GameService;
import chess.service.RoomService;
import chess.web.dto.BoardDto;
import chess.web.dto.RoomDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;
    private final GameService gameService;

    public RoomController(RoomService roomService, GameService gameService) {
        this.roomService = roomService;
        this.gameService = gameService;
    }

    @GetMapping
    public ResponseEntity<List<RoomDto>> loadRooms() {
        List<RoomDto> rooms = roomService.findAll();
        return ResponseEntity.ok(rooms);
    }

    @PostMapping
    public String createRoom(@RequestParam String name) {
        RoomDto roomDto = roomService.create(name);
        return "redirect:/rooms/" + roomDto.getId();
    }

    @GetMapping("/{roomId}")
    public String board(@PathVariable int roomId) {
        roomService.validateId(roomId);
        return "/board.html";
    }

    @DeleteMapping("/{roomId}")
    public String deleteRoom(@PathVariable int roomId) {
        roomService.removeById(roomId);
        return "redirect:/";
    }

    @GetMapping("/{roomId}/start")
    public ResponseEntity<BoardDto> startNewGame(@PathVariable int roomId) {
        return ResponseEntity.ok(gameService.startNewGame(roomId));
    }

    @GetMapping("/{roomId}/load")
    public ResponseEntity<BoardDto> loadGame(@PathVariable int roomId) {
        return ResponseEntity.ok(gameService.loadGame(roomId));
    }
}
