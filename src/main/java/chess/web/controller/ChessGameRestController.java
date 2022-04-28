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
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/start/{roomId}")
    public RoomDto start(@PathVariable("roomId") int roomId) {
        return service.start(roomId);
    }

    @PostMapping("/play/{roomId}")
    public PlayResultDto play(@PathVariable("roomId") int roomId) {
        return service.play(roomId);
    }

    @PostMapping("/move")
    public MoveResultDto move(@RequestBody MoveDto moveDto) {
        return service.move(moveDto, 1);
    }

    @PostMapping("/create")
    public CreateRoomResultDto createRoom(@RequestBody CreateRoomRequestDto createRoomRequestDto) {
        return service.createRoom(createRoomRequestDto);
    }

    @GetMapping("/rooms")
    public ReadRoomResultDto readRooms() {
        return service.findAllRooms();
    }

    @PostMapping("/room/{roomId}")
    public DeleteResultDto delete(@PathVariable("roomId") int roomId, @RequestBody DeleteDto deleteDto) {
        return service.delete(roomId, deleteDto);
    }
}
