package chess.controller;

import chess.dto.web.BoardDto;
import chess.dto.web.GameStatusDto;
import chess.dto.web.MovementDto;
import chess.dto.web.PointDto;
import chess.dto.web.RoomDto;
import chess.dto.web.RoomStatusDto;
import chess.dto.web.UsersInRoomDto;
import chess.service.ChessService;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
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
        RoomDto responseBody = chessService.create(roomDto);
        return ResponseEntity.created(URI.create("/rooms/" + responseBody.getId()))
            .body(responseBody);
    }

    @PutMapping("{id}/status")
    private ResponseEntity<Void> closeRoom(@PathVariable String id,
        @RequestBody RoomStatusDto roomStatusDto) {
        if (roomStatusDto.getStatus().equals("closed")) {
            chessService.close(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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
    private ResponseEntity<BoardDto> updateStatus(@PathVariable String id,
        @RequestBody GameStatusDto gameStatusDto) {
        if (gameStatusDto.getGameState().equals("Running")) {
            return ResponseEntity.ok(chessService.start(id));
        }
        if (gameStatusDto.getGameState().equals("Finished")) {
            return ResponseEntity.ok(chessService.exit(id));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("{id}/points/{point}/movable-points")
    private List<PointDto> movablePoints(@PathVariable String id, @PathVariable String point) {
        return chessService.movablePoints(id, point);
    }

    @PutMapping("{id}/movement")
    private ResponseEntity<BoardDto> move(@PathVariable String id,
        @RequestBody MovementDto movementDto) {
        return ResponseEntity
            .ok(chessService.move(id, movementDto.getSource(), movementDto.getDestination()));
    }
}
