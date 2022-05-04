package chess.web.controller;

import chess.web.dto.CreateRoomDto;
import chess.web.dto.MoveDto;
import chess.web.dto.PasswordDto;
import chess.web.dto.PlayResultDto;
import chess.web.dto.RoomDto;
import chess.web.dto.ScoreDto;
import chess.web.service.ChessGameService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessGameRestController {

    private final ChessGameService service;

    public ChessGameRestController(ChessGameService service) {
        this.service = service;
    }

    @PutMapping("/chess-game")
    public ResponseEntity<PlayResultDto> move(@RequestBody MoveDto moveDto) {
        final PlayResultDto playResultDto = service.move(moveDto);
        return ResponseEntity.ok(playResultDto);
    }

    @PostMapping("/chess-game")
    public ResponseEntity createRoom(@RequestBody CreateRoomDto createRoomDto) {
        final RoomDto roomDto = service.createRoom(createRoomDto);
        return new ResponseEntity(roomDto, HttpStatus.CREATED);
    }

    @GetMapping("/chess-games")
    public ResponseEntity<List<RoomDto>> loadRoom() {
        final List<RoomDto> roomDtos = service.loadChessGames();
        return ResponseEntity.ok(roomDtos);
    }

    @GetMapping("/chess-game/{id}/board")
    public ResponseEntity<PlayResultDto> loadChessBoard(@PathVariable int id) {
        final PlayResultDto playResultDto = service.play(id);
        return ResponseEntity.ok(playResultDto);
    }

    @GetMapping("/chess-game/{id}/initialization")
    public ResponseEntity initialize(@PathVariable int id) {
        service.start(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/chess-game/{id}/status")
    public ResponseEntity<ScoreDto> status(@PathVariable int id) {
        final ScoreDto scoreDto = service.status(id);
        return ResponseEntity.ok(scoreDto);
    }

    @DeleteMapping("/chess-game/{id}")
    public ResponseEntity deleteRoom(@PathVariable int id) {
        service.deleteRoomById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/chess-game/{id}/password")
    public ResponseEntity confirmPassword(@PathVariable int id, @RequestBody PasswordDto password) {
        boolean result = service.confirmPassword(id, password.getPassword());
        if (!result) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
