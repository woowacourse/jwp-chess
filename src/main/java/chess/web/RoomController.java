package chess.web;

import java.util.List;
import java.util.stream.Collectors;

import chess.service.GameService;
import chess.service.RoomService;
import chess.web.dto.BoardDto;
import chess.web.dto.RoomDto;
import chess.web.dto.RoomResponseDto;

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
    public ResponseEntity<List<RoomResponseDto>> loadRooms() {
        List<RoomResponseDto> rooms = roomService.findAll().stream()
            .map(room -> new RoomResponseDto(room.getId(), room.getName()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(rooms);
    }

    @PostMapping
    public String createRoom(@RequestParam String name, @RequestParam String password) {
        RoomDto room = roomService.create(new RoomDto(name, password));
        return "redirect:/rooms/" + room.getId();
    }

    @GetMapping("/{roomId}")
    public String board(@PathVariable int roomId) {
        roomService.validateId(roomId);
        return "/board.html";
    }

    @DeleteMapping("/{roomId}")
    public String deleteRoom(@PathVariable int roomId, @RequestParam String password) {
        roomService.delete(roomId, password);
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
