package chess.controller;

import chess.dto.web.BoardDto;
import chess.dto.web.GameStatusDto;
import chess.dto.web.PointDto;
import chess.dto.web.RoomDto;
import chess.dto.web.UsersInRoomDto;
import chess.service.ChessService;
import java.util.List;
import java.util.Map;
import org.eclipse.jetty.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
public class SpringChessApiController {

    private final ChessService chessService;

    public SpringChessApiController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping
    private ResponseEntity<RoomDto> createRoom(@RequestBody RoomDto roomDto) {
        return ResponseEntity.status(HttpStatus.CREATED_201).body(chessService.create(roomDto));
    }

    @PutMapping
    private ResponseEntity<Object> closeRoom(@RequestBody RoomDto roomDto) {
        chessService.close(roomDto.getId());
        return ResponseEntity.status(HttpStatus.CREATED_201).build();
    }

    @GetMapping("{id}/statistics")
    private UsersInRoomDto usersInRoom(@PathVariable String id) {
        return chessService.usersInRoom(id);
    }

    @GetMapping("{id}/game-status")
    private GameStatusDto gameStatus(@PathVariable String id) {
        return chessService.gameStatus(id);
    }

    @PutMapping("{id}/game-status")
    private ResponseEntity<BoardDto> updateStatus(@PathVariable String id, @RequestBody GameStatusDto gameStatusDto) {
        if (gameStatusDto.getGameState().equals("Running")) {
            return ResponseEntity.status(HttpStatus.CREATED_201).body(chessService.start(id));
        }
        if (gameStatusDto.getGameState().equals("Finished")) {
            return ResponseEntity.status(HttpStatus.CREATED_201).body(chessService.exit(id));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST_400).build();
    }

    @GetMapping("/{id}/points/{point}/movable-points")
    private List<PointDto> movablePoints(@PathVariable String id, @PathVariable String point) {
        return chessService.movablePoints(id, point);
    }

    @PostMapping("{id}/move")
    private BoardDto move(@PathVariable String id, @RequestBody Map<String, String> body) {
        return chessService.move(id, body.get("source"), body.get("destination"));
    }
}
