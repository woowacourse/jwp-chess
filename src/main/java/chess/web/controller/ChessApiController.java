package chess.web.controller;

import chess.board.Board;
import chess.web.service.ChessService;
import chess.web.service.dto.BoardDto;
import chess.web.service.dto.CreateBoardDto;
import chess.web.service.dto.MoveDto;
import chess.web.service.dto.ScoreDto;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class ChessApiController {

    private final ChessService chessService;

    public ChessApiController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping(value = "/board", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> createBoard(@RequestBody CreateBoardDto createBoardDto) {
        Long boardId = chessService.createBoard(createBoardDto.getTitle(), createBoardDto.getPassword());
        return ResponseEntity.created(URI.create("/board/" + boardId)).body(boardId);
    }

    @PutMapping(value = "/board/{boardId}/initialization", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardDto> initBoard(@PathVariable Long boardId) {
        Board board = chessService.initBoard(boardId);
        return ResponseEntity.ok().body(BoardDto.from(board));
    }

    @PatchMapping(value = "/board/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardDto> move(@RequestBody MoveDto moveDto, @PathVariable Long boardId) {
        Board board = chessService.move(moveDto, boardId);
        return ResponseEntity.ok().body(BoardDto.from(board));
    }

    @GetMapping(value = "/board/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardDto> loadGame(@PathVariable Long boardId) {
        Board board = chessService.loadGame(boardId);
        return ResponseEntity.ok().body(BoardDto.from(board));
    }

    @GetMapping(value = "/board/{boardId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScoreDto> getStatus(@PathVariable Long boardId) {
        ScoreDto status = chessService.getStatus(boardId);
        return ResponseEntity.ok().body(status);
    }

    @DeleteMapping(value = "/board/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> deleteRoom(@RequestBody String password, @PathVariable Long boardId) {
        boolean result = chessService.removeRoom(boardId, password);
        return ResponseEntity.ok().body(result);
    }
}
