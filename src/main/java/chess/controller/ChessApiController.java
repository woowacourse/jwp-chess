package chess.controller;

import chess.domain.Score;
import chess.dto.ChessPieceDto;
import chess.dto.CurrentTurnDto;
import chess.dto.ErrorResponseDto;
import chess.dto.MoveRequestDto;
import chess.dto.RoomNameDto;
import chess.result.EndResult;
import chess.result.MoveResult;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
public class ChessApiController {

    private final RoomService roomService;
    private final ChessService chessService;

    public ChessApiController(final RoomService roomService, final ChessService chessService) {
        this.roomService = roomService;
        this.chessService = chessService;
    }

    @GetMapping
    public ResponseEntity<List<RoomNameDto>> findAllRoomName() {
        final List<RoomNameDto> roomNames = roomService.findAllRoomName();
        return ResponseEntity.ok(roomNames);
    }

    @PostMapping
    public ResponseEntity createRoom(@RequestBody final RoomNameDto roomNameDto) {
        roomService.createRoom(roomNameDto);
        return ResponseEntity.created(URI.create("/rooms/" + roomNameDto.getName())).build();
    }

    @DeleteMapping("/{roomName}")
    public ResponseEntity deleteRoom(@PathVariable final String roomName) {
        roomService.deleteRoom(roomName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{roomName}/pieces")
    public ResponseEntity<List<ChessPieceDto>> findPieces(@PathVariable final String roomName) {
        final List<ChessPieceDto> chessPieces = chessService.findAllPiece(roomName);
        return ResponseEntity.ok(chessPieces);
    }

    @PostMapping("/{roomName}/pieces")
    public ResponseEntity createPieces(@PathVariable final String roomName) {
        chessService.initPiece(roomName);
        return ResponseEntity.created(URI.create("/rooms/" + roomName + "/pieces")).build();
    }

    @PatchMapping("/{roomName}/pieces")
    public ResponseEntity<MoveResult> movePiece(@PathVariable final String roomName,
                                                @RequestBody final MoveRequestDto moveRequestDto) {
        final MoveResult moveResult = chessService.move(roomName, moveRequestDto);
        return ResponseEntity.ok(moveResult);
    }

    @GetMapping("/{roomName}/scores")
    public ResponseEntity<Score> findScore(@PathVariable final String roomName) {
        final Score score = chessService.findScore(roomName);
        return ResponseEntity.ok(score);
    }

    @GetMapping("/{roomName}/turn")
    public ResponseEntity<CurrentTurnDto> findTurn(@PathVariable final String roomName) {
        final CurrentTurnDto currentTurn = roomService.findCurrentTurn(roomName);
        return ResponseEntity.ok(currentTurn);
    }

    @GetMapping("/{roomName}/result")
    public ResponseEntity<EndResult> findResult(@PathVariable final String roomName) {
        final EndResult endResult = chessService.result(roomName);
        return ResponseEntity.ok(endResult);
    }
}
