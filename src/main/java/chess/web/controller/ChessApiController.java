package chess.web.controller;

import chess.board.Board;
import chess.web.service.ChessService;
import chess.web.service.dto.BoardDto;
import chess.web.service.dto.CreateRoomDto;
import chess.web.service.dto.MoveDto;
import chess.web.service.dto.ScoreDto;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessApiController {

    private final ChessService chessService;

    public ChessApiController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping(value = "/chess", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> createChess(@RequestBody CreateRoomDto createRoomDto) {
        Long roomId = chessService.createRoom(createRoomDto.getTitle(), createRoomDto.getPassword());
        return ResponseEntity.created(URI.create("/chess/" + roomId)).body(roomId);
    }

    @GetMapping(value = "/chess/{roomId}/load", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardDto> loadGame(@PathVariable Long roomId) {
        Board board = chessService.loadGame(roomId);
        return ResponseEntity.ok().body(BoardDto.from(board));
    }

    @GetMapping(value = "/chess/{roomId}/restart", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardDto> initBoard(@PathVariable Long roomId) {
        Board board = chessService.initBoard(roomId);
        return ResponseEntity.ok().body(BoardDto.from(board));
    }

    @GetMapping(value = "/chess/{roomId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScoreDto> getStatus(@PathVariable Long roomId) {
        ScoreDto status = chessService.getStatus(roomId);
        return ResponseEntity.ok().body(status);
    }

    @PostMapping(value = "/chess/{roomId}/move", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardDto> move(@RequestBody MoveDto moveDto, @PathVariable Long roomId) {
        Board board = chessService.move(moveDto, roomId);
        return ResponseEntity.ok().body(BoardDto.from(board));
    }

    @DeleteMapping(value = "/chess/{roomId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> deleteRoom(@RequestBody String password, @PathVariable Long roomId) {
        boolean result = chessService.removeRoom(roomId, password);
        return ResponseEntity.ok().body(result);
    }
}
