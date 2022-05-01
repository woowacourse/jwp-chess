package chess.web.controller;

import chess.web.dto.CreateRoomDto;
import chess.web.dto.MoveDto;
import chess.web.dto.PlayResultDto;
import chess.web.dto.RoomDto;
import chess.web.service.ChessGameService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessGameRestController {

    private final ChessGameService service;

    public ChessGameRestController(ChessGameService service) {
        this.service = service;
    }

    @GetMapping("/load")
    public ResponseEntity<PlayResultDto> load() {
        final PlayResultDto playResultDto = service.play();
        return ResponseEntity.ok(playResultDto);
    }

    @PostMapping("/move")
    public ResponseEntity<PlayResultDto> move(@RequestBody MoveDto moveDto) {
        final PlayResultDto playResultDto = service.move(moveDto);;
        return ResponseEntity.ok(playResultDto);
    }

    @PostMapping("/chess-game")
    public ResponseEntity createRoom(@RequestBody CreateRoomDto createRoomDto) {
        final RoomDto roomDto = service.createRoom(createRoomDto);
        return new ResponseEntity(roomDto, HttpStatus.CREATED);
    }

    @GetMapping("/chess-game")
    public ResponseEntity<List<RoomDto>> loadChessGames() {
        List<RoomDto> roomDtos = service.loadChessGames();
        return ResponseEntity.ok(roomDtos);
    }
}
