package chess.web.controller;

import chess.domain.board.Board;
import chess.web.controller.dto.*;
import chess.web.service.ChessService;
import chess.web.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequestMapping("/api/chess")
@RestController
public class ChessApiController {

    private final ChessService chessService;
    private final RoomService roomService;

    public ChessApiController(ChessService chessService, RoomService roomService) {
        this.chessService = chessService;
        this.roomService = roomService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDto> loadGame(@PathVariable Long id) {
        Board board = chessService.loadGame(id);
        return ResponseEntity.ok().body(BoardDto.from(id, board));
    }

    @PostMapping("/new")
    public ResponseEntity<Void> createGame(@RequestBody RoomRequest.TitleAndPassword request) {
        Long id = roomService.createRoom(request.getTitle(), request.getPassword());
        return ResponseEntity.created(URI.create("/room/" + id)).build();
    }

    @GetMapping("/{id}/restart")
    public ResponseEntity<BoardDto> initBoard(@PathVariable Long id) {
        Board board = chessService.initBoard(id);
        return ResponseEntity.ok().body(BoardDto.from(id, board));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<ScoreDto> getStatus(@PathVariable Long id) {
        ScoreDto status = chessService.getStatus(id);
        return ResponseEntity.ok().body(status);
    }

    @PostMapping("/{id}/move")
    public ResponseEntity<BoardDto> move(@RequestBody MoveDto moveDto, @PathVariable Long id) {
        Board board = chessService.move(moveDto, id);
        return ResponseEntity.ok().body(BoardDto.from(id, board));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestBody RoomRequest.Password request, @PathVariable Long id) {
        roomService.delete(request.getPassword(), id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<Void> getJoin(@RequestBody RoomRequest.Password request, @PathVariable Long id) {
        roomService.checkJoinRoom(request.getPassword(), id);
        return ResponseEntity.ok().build();
    }
}
