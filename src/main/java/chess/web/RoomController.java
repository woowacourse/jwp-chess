package chess.web;

import chess.domain.Room;
import chess.service.GameService;
import chess.service.RoomService;
import chess.web.dto.BoardDto;
import chess.web.dto.ResultDto;
import chess.web.dto.RoomDto;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public ResponseEntity<List<Map<String, String>>> findRooms() {
        return ResponseEntity.ok(roomService.findRooms());
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createRoom(Room room) {
        RoomDto roomDto = roomService.create(room);
        return ResponseEntity.ok(Map.of("url","/rooms/" + roomDto.getId()));
    }

    @GetMapping("/{roomId}")
    public String board(@PathVariable int roomId) {
        roomService.validateId(roomId);
        return "/board.html";
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Map<String, String>> deleteRoom(@PathVariable int roomId, @RequestParam String password) {
        Optional<Integer> boardId = gameService.findByRoomId(roomId);
        if (boardId.isEmpty()) {
            roomService.delete(roomId, password);
            return ResponseEntity.ok(Map.of("url","/"));
        }
        BoardDto boardDto = gameService.loadGame(roomId);
        BoardDto boardDto1 = gameService.gameStateAndPieces(boardDto.getBoardId());
        if (!boardDto1.getState().getEnd()) {
            throw new IllegalArgumentException("진행 중인 게임방은 삭제할 수 없습니다.");
        }
        roomService.delete(roomId, password);
        return ResponseEntity.ok(Map.of("url","/"));
    }

    @GetMapping(value = "/{roomId}", params = "command=start")
    public ResponseEntity<BoardDto> startNewGame(@PathVariable int roomId) {
        return ResponseEntity.ok(gameService.startNewGame(roomId));
    }

    @GetMapping(value = "/{roomId}", params = "command=load")
    public ResponseEntity<BoardDto> loadGame(@PathVariable int roomId) {
        return ResponseEntity.ok(gameService.loadGame(roomId));
    }
}
