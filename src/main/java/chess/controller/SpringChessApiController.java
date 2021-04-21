package chess.controller;

import chess.dto.web.BoardDto;
import chess.dto.web.GameStatusDto;
import chess.dto.web.PointDto;
import chess.dto.web.RoomDto;
import chess.dto.web.UsersInRoomDto;
import chess.service.ChessService;
import java.util.HashMap;
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
        roomDto.setId(chessService.create(roomDto));
        return ResponseEntity.status(HttpStatus.CREATED_201).body(roomDto);
    }

    @PutMapping("/{id}/close")
    private Map<String, String> closeRoom(@PathVariable String id) {
        Map<String, String> result = new HashMap<>();
        chessService.close(id);
        result.put("result", "success");
        return result;
    }

    @GetMapping("{id}/statistics")
    private UsersInRoomDto usersInRoom(@PathVariable String id) {
        return chessService.usersInRoom(id);
    }

    @GetMapping("{id}/getGameStatus")
    private GameStatusDto gameStatus(@PathVariable String id) {
        return chessService.gameStatus(id);
    }

    @PutMapping("{id}/start")
    private BoardDto startGame(@PathVariable String id) {
        return chessService.start(id);
    }

    @PutMapping("{id}/exit")
    private Map<String, String> exitGame(@PathVariable String id) {
        Map<String, String> result = new HashMap<>();
        chessService.exit(id);
        result.put("result", "success");
        return result;
    }

    @GetMapping("/{id}/movablePoints/{point}")
    private List<PointDto> movablePoints(@PathVariable String id, @PathVariable String point) {
        return chessService.movablePoints(id, point);
    }

    @PostMapping("{id}/move")
    private BoardDto move(@PathVariable String id, @RequestBody Map<String, String> body) {
        return chessService.move(id, body.get("source"), body.get("destination"));
    }
}
