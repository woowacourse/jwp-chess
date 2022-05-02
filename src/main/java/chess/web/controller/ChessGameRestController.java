package chess.web.controller;

import chess.web.dto.CreateRoomRequestDto;
import chess.web.dto.CreateRoomResultDto;
import chess.web.dto.DeleteDto;
import chess.web.dto.DeleteResultDto;
import chess.web.dto.MoveDto;
import chess.web.dto.MoveResultDto;
import chess.web.dto.PlayResultDto;
import chess.web.dto.ReadRoomResultDto;
import chess.web.dto.RoomDto;
import chess.web.service.ChessGameService;
import java.util.HashMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessGameRestController {

    private final ChessGameService service;

    public ChessGameRestController(ChessGameService service) {
        this.service = service;
    }

    @PostMapping("/start/{roomId}")
    public RoomDto start(@PathVariable int roomId) {
        return service.start(roomId);
    }

    @PostMapping("/play/{roomId}")
    public PlayResultDto play(@PathVariable int roomId) {
        return service.play(roomId);
    }

    @PatchMapping("/rooms/{roomId}")
    public MoveResultDto move(@PathVariable int roomId, @RequestBody MoveDto moveDto) {
        return service.move(moveDto, roomId);
    }

    @PostMapping("/rooms")
    public CreateRoomResultDto createRoom(@RequestBody CreateRoomRequestDto createRoomRequestDto) {
        return service.createRoom(createRoomRequestDto);
    }

    @GetMapping("/rooms")
    public ReadRoomResultDto readRooms() {
        return service.findAllRooms();
    }

    @DeleteMapping("/rooms/{roomId}")
    public DeleteResultDto delete(@PathVariable int roomId, @RequestBody DeleteDto deleteDto) {
        return service.delete(roomId, deleteDto);
    }
}
