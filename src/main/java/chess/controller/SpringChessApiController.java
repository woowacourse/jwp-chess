package chess.controller;

import chess.dto.web.*;
import chess.service.SpringChessService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/room")
public class SpringChessApiController {

    private SpringChessService springChessService;

    public SpringChessApiController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    @PostMapping
    private Map<String, String> createRoom(@RequestBody RoomDto roomDto) {
        Map<String, String> result = new HashMap<>();
        result.put("result", "success");
        result.put("roomId", springChessService.create(roomDto));
        return result;
    }

    @GetMapping("{id}/statistics")
    private UsersInRoomDto usersInRoom(@PathVariable String id) {
        return springChessService.usersInRoom(id);
    }

    @GetMapping("{id}/getGameStatus")
    private GameStatusDto gameStatus(@PathVariable String id) {
        return springChessService.gameStatus(id);
    }

    @PutMapping("{id}/start")
    private BoardDto startGame(@PathVariable String id) {
        return springChessService.start(id);
    }

    @PutMapping("{id}/exit")
    private Map<String, String> exitGame(@PathVariable String id) {
        Map<String, String> result = new HashMap<>();
        springChessService.exit(id);
        result.put("result", "success");
        return result;
    }

    @PutMapping("/{id}/close")
    private Map<String, String> closeRoom(@PathVariable String id) {
        Map<String, String> result = new HashMap<>();
        springChessService.close(id);
        result.put("result", "success");
        return result;
    }

    @GetMapping("/{id}/movablePoints/{point}")
    private List<PointDto> movablePoints(@PathVariable String id, @PathVariable String point) {
        return springChessService.movablePoints(id, point);
    }

    @PostMapping("{id}/move")
    private BoardDto move(@PathVariable String id, @RequestBody Map<String, String> body) {
        return springChessService.move(id, body.get("source"), body.get("destination"));
    }
}
