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

    @GetMapping(value = "/chess/{boardId}/load", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardDto> loadGame(@PathVariable Long boardId) {
        Board board = chessService.loadGame(boardId);
        return ResponseEntity.ok().body(BoardDto.from(board));
    }

    @GetMapping(value = "/chess/{boardId}/restart", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardDto> initBoard(@PathVariable Long boardId) {
        Board board = chessService.initBoard(boardId);
        return ResponseEntity.ok().body(BoardDto.from(board));
    }

    @GetMapping(value = "/chess/{boardId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScoreDto> getStatus(@PathVariable Long boardId) {
        ScoreDto status = chessService.getStatus(boardId);
        return ResponseEntity.ok().body(status);
    }

    @PostMapping(value = "/chess/{boardId}/move", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardDto> move(@RequestBody MoveDto moveDto, @PathVariable Long boardId) {
        Board board = chessService.move(moveDto, boardId);
        return ResponseEntity.ok().body(BoardDto.from(board));
    }

    @DeleteMapping(value = "/chess/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> deleteRoom(@RequestBody String password, @PathVariable Long boardId) {
        boolean result = chessService.removeRoom(boardId, password);
        return ResponseEntity.ok().body(result);
    }
}
