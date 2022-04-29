package chess.web.controller;

import chess.domain.board.Board;
import chess.web.controller.dto.BoardDto;
import chess.web.controller.dto.MoveDto;
import chess.web.controller.dto.RoomRequestDto;
import chess.web.controller.dto.ScoreDto;
import chess.web.service.ChessService;
import chess.web.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
public class ChessApiController {

    private final ChessService chessService;
    private final RoomService roomService;

    public ChessApiController(ChessService chessService, RoomService roomService) {
        this.chessService = chessService;
        this.roomService = roomService;
    }

    @GetMapping("/load/{id}")
    public ResponseEntity<BoardDto> loadGame(@PathVariable Long id) {
        Board board = chessService.loadGame(id);
        return ResponseEntity.ok().body(BoardDto.from(id, board));
    }

    @GetMapping("/restart/{id}")
    public ResponseEntity<BoardDto> initBoard(@PathVariable Long id) {
        Board board = chessService.initBoard(id);
        return ResponseEntity.ok().body(BoardDto.from(id, board));
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<ScoreDto> getStatus(@PathVariable Long id) {
        ScoreDto status = chessService.getStatus(id);
        return ResponseEntity.ok().body(status);
    }

    @PostMapping("/move/{id}")
    public ResponseEntity<BoardDto> move(@RequestBody MoveDto moveDto, @PathVariable Long id) {
        Board board = chessService.move(moveDto, id);
        return ResponseEntity.ok().body(BoardDto.from(id, board));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestBody RoomRequestDto.Password request, @PathVariable Long id) {
        roomService.delete(request.getPassword(), id);
        return ResponseEntity.ok().build();
    }
}
