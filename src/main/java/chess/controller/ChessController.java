package chess.controller;

import chess.domain.Score;
import chess.dto.ChessPieceDto;
import chess.dto.CurrentTurnDto;
import chess.dto.ErrorResponseDto;
import chess.dto.MoveRequestDto;
import chess.result.EndResult;
import chess.result.MoveResult;
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
@RequestMapping("/rooms")
public class ChessController {

    private final RoomService roomService;
    private final ChessService chessService;

    public ChessController(final RoomService roomService, final ChessService chessService) {
        this.roomService = roomService;
        this.chessService = chessService;
    }

    @PostMapping("/{roomName}")
    public void createRoom(@PathVariable("roomName") final String roomName) {
        roomService.createRoom(roomName);
    }

    @DeleteMapping("/{roomName}")
    public void deleteRoom(@PathVariable("roomName") final String roomName) {
        roomService.deleteRoom(roomName);
    }

    @GetMapping("/{roomName}/pieces")
    public ResponseEntity<List<ChessPieceDto>> findPieces(@PathVariable("roomName") final String roomName) {
        final List<ChessPieceDto> chessPieces = chessService.findAllPiece(roomName);
        return ResponseEntity.ok(chessPieces);
    }

    @PostMapping("/{roomName}/pieces")
    public void createPieces(@PathVariable("roomName") final String roomName) {
        chessService.initPiece(roomName);
    }

    @PutMapping("/{roomName}/pieces")
    public ResponseEntity<MoveResult> movePiece(@PathVariable("roomName") final String roomName,
                                                @RequestBody final MoveRequestDto moveRequestDto) {
        final MoveResult moveResult = chessService.move(roomName, moveRequestDto);
        return ResponseEntity.ok(moveResult);
    }

    @GetMapping("/{roomName}/scores")
    public ResponseEntity<Score> findScore(@PathVariable("roomName") final String roomName) {
        final Score score = chessService.findScore(roomName);
        return ResponseEntity.ok(score);
    }

    @GetMapping("/{roomName}/turn")
    public ResponseEntity<CurrentTurnDto> findTurn(@PathVariable("roomName") final String roomName) {
        final CurrentTurnDto currentTurn = roomService.findCurrentTurn(roomName);
        return ResponseEntity.ok(currentTurn);
    }

    @GetMapping("/{roomName}/result")
    public ResponseEntity<EndResult> findResult(@PathVariable("roomName") final String roomName) {
        final EndResult endResult = chessService.result(roomName);
        return ResponseEntity.ok(endResult);
    }

    @ExceptionHandler({IllegalArgumentException.class, DataAccessException.class,
            InvalidResultSetAccessException.class})
    public ResponseEntity<ErrorResponseDto> handle(final Exception e) {
        return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
    }
}
