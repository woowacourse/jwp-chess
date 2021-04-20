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
    private void exitGame(@PathVariable String id) {
        springChessService.exit(id);
    }

    @PutMapping("/{id}/close")
    private void closeRoom(@PathVariable String id) {
        springChessService.close(id);
    }

    @GetMapping("/{id}/movablePoints/{point}")
    private List<PointDto> movablePoints(@PathVariable String id, @PathVariable String point) {
        return springChessService.movablePoints(id, point);
    }

    @PostMapping("{id}/move")
    private BoardDto move(@PathVariable String id, @RequestBody PointsDto pointsDto) {
        return springChessService.move(id, pointsDto.getSource(), pointsDto.getDestination());
    }
}
