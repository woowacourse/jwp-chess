package chess.controller;

import chess.domain.Score;
import chess.domain.result.EndResult;
import chess.domain.result.MoveResult;
import chess.dto.ChessPieceDto;
import chess.dto.CurrentTurnDto;
import chess.dto.ErrorResponseDto;
import chess.dto.MoveRequestDto;
import chess.service.ChessService;
import chess.service.RoomService;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms/{roomName}")
public class ChessController {

    private final RoomService roomService;
    private final ChessService chessService;

    public ChessController(final RoomService roomService, final ChessService chessService) {
        this.roomService = roomService;
        this.chessService = chessService;
    }

    @PostMapping
    public void createRoom(@PathVariable final String roomName) {
        roomService.createRoom(roomName);
    }

    @DeleteMapping
    public void deleteRoom(@PathVariable final String roomName) {
        roomService.deleteRoom(roomName);
    }

    @GetMapping("/pieces")
    public ResponseEntity<List<ChessPieceDto>> findPieces(@PathVariable final String roomName) {
        final List<ChessPieceDto> chessPieces = chessService.findAllPiece(roomName);
        return ResponseEntity.ok(chessPieces);
    }

    @PostMapping("/pieces")
    public void createPieces(@PathVariable final String roomName) {
        chessService.initPiece(roomName);
    }

    @PutMapping("/pieces")
    public ResponseEntity<MoveResult> movePiece(@PathVariable final String roomName,
                                                @RequestBody final MoveRequestDto moveRequestDto) {
        final MoveResult moveResult = chessService.move(roomName, moveRequestDto);
        return ResponseEntity.ok(moveResult);
    }

    @GetMapping("/scores")
    public ResponseEntity<Score> findScore(@PathVariable final String roomName) {
        final Score score = chessService.findScore(roomName);
        return ResponseEntity.ok(score);
    }

    @GetMapping("/turn")
    public ResponseEntity<CurrentTurnDto> findTurn(@PathVariable final String roomName) {
        final CurrentTurnDto currentTurn = roomService.findCurrentTurn(roomName);
        return ResponseEntity.ok(currentTurn);
    }

    @GetMapping("/result")
    public ResponseEntity<EndResult> findResult(@PathVariable final String roomName) {
        final EndResult endResult = chessService.result(roomName);
        return ResponseEntity.ok(endResult);
    }

    @ExceptionHandler({IllegalArgumentException.class, DataAccessException.class,
            InvalidResultSetAccessException.class})
    public ResponseEntity<ErrorResponseDto> handle(final Exception e) {
        return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
    }
}
