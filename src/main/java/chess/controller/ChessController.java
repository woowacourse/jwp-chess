package chess.controller;

import chess.domain.Score;
import chess.domain.result.EndResult;
import chess.domain.result.MoveResult;
import chess.dto.ChessPieceDto;
import chess.dto.CurrentTurnDto;
import chess.dto.ErrorResponseDto;
import chess.dto.MoveRequestDto;
import chess.dto.RoomCreationRequestDto;
import chess.dto.RoomDeletionRequestDto;
import chess.dto.RoomResponseDto;
import chess.service.ChessService;
import chess.service.RoomService;
import java.net.URI;
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

    @GetMapping
    public List<RoomResponseDto> findAllRoom() {
        return roomService.findAll();
    }

    @PostMapping
    public ResponseEntity<Object> createRoom(@RequestBody final RoomCreationRequestDto dto) {
        final int roomId = roomService.createRoom(dto);
        return ResponseEntity.created(URI.create("/rooms/" + roomId)).build();
    }

    @DeleteMapping
    public void deleteRoom(@RequestBody RoomDeletionRequestDto dto) {
        roomService.deleteRoom(dto);
    }

    @GetMapping("/{roomId}/pieces")
    public ResponseEntity<List<ChessPieceDto>> findPieces(@PathVariable final int roomId) {
        final List<ChessPieceDto> chessPieces = chessService.findAllPiece(roomId);
        return ResponseEntity.ok(chessPieces);
    }

    @PostMapping("/{roomId}/pieces")
    public void createPieces(@PathVariable final int roomId) {
        chessService.initPiece(roomId);
    }

    @PutMapping("/{roomId}/pieces")
    public ResponseEntity<MoveResult> movePiece(@PathVariable final int roomId,
                                                @RequestBody final MoveRequestDto moveRequestDto) {
        final MoveResult moveResult = chessService.move(roomId, moveRequestDto);
        return ResponseEntity.ok(moveResult);
    }

    @GetMapping("/{roomId}/scores")
    public ResponseEntity<Score> findScore(@PathVariable final int roomId) {
        final Score score = chessService.findScore(roomId);
        return ResponseEntity.ok(score);
    }

    @GetMapping("/{roomId}/turn")
    public ResponseEntity<CurrentTurnDto> findTurn(@PathVariable final int roomId) {
        final CurrentTurnDto currentTurn = roomService.findCurrentTurn(roomId);
        return ResponseEntity.ok(currentTurn);
    }

    @GetMapping("/{roomId}/result")
    public ResponseEntity<EndResult> findResult(@PathVariable final int roomId) {
        final EndResult endResult = chessService.result(roomId);
        return ResponseEntity.ok(endResult);
    }

    @ExceptionHandler({IllegalArgumentException.class, DataAccessException.class,
            InvalidResultSetAccessException.class})
    public ResponseEntity<ErrorResponseDto> handle(final Exception e) {
        return ResponseEntity.badRequest().body(new ErrorResponseDto(e.getMessage()));
    }
}
