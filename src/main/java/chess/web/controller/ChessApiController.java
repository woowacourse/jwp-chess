package chess.web.controller;

import chess.board.Board;
import chess.web.controller.dto.RoomRequestDto;
import chess.web.service.ChessService;
import chess.web.controller.dto.BoardDto;
import chess.web.controller.dto.MoveDto;
import chess.web.controller.dto.ScoreDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequestMapping("/api")
@RestController
public class ChessApiController {

    private final ChessService chessService;

    public ChessApiController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("/room")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createGame(@RequestBody RoomRequestDto roomRequestDto){
        Long id = chessService.createRoom(roomRequestDto);
        return ResponseEntity.created(URI.create("/api/load" + id)).build();
    }
    @GetMapping("/load/{id}")
    public ResponseEntity<BoardDto> loadGame(@PathVariable Long id) {
        Board board = chessService.loadGame(id);
        return ResponseEntity.ok().body(BoardDto.from(board));
    }

    @GetMapping("/restart/{id}")
    public ResponseEntity<BoardDto> initBoard(@PathVariable Long id) {
        Board board = chessService.initBoard(id);
        return ResponseEntity.ok().body(BoardDto.from(board));
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<ScoreDto> getStatus(@PathVariable Long id) {
        ScoreDto status = chessService.getStatus(id);
        return ResponseEntity.ok().body(status);
    }

    @PostMapping("/move/{id}")
    public ResponseEntity<BoardDto> move(@RequestBody MoveDto moveDto, @PathVariable Long id) {
        Board board = chessService.move(moveDto, id);
        return ResponseEntity.ok().body(BoardDto.from(board));
    }
}
