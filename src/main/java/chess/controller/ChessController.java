package chess.controller;

import chess.domain.Score;
import chess.domain.result.EndResult;
import chess.dto.request.MoveRequestDto;
import chess.dto.request.RoomCreationRequestDto;
import chess.dto.request.RoomDeletionRequestDto;
import chess.dto.response.ChessPieceDto;
import chess.dto.response.CurrentTurnDto;
import chess.dto.response.RoomResponseDto;
import chess.service.ChessService;
import chess.service.RoomService;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoom(@RequestBody RoomDeletionRequestDto dto) {
        roomService.deleteRoom(dto);
    }

    @PatchMapping("/{roomId}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void startGame(@PathVariable final int roomId) {
        roomService.startGame(roomId);
    }

    @GetMapping("/{roomId}/pieces")
    public ResponseEntity<List<ChessPieceDto>> findPieces(@PathVariable final int roomId) {
        final List<ChessPieceDto> chessPieces = chessService.findAllPiece(roomId);
        return ResponseEntity.ok(chessPieces);
    }

    @PostMapping("/{roomId}/pieces")
    @ResponseStatus(HttpStatus.CREATED)
    public void createPieces(@PathVariable final int roomId) {
        chessService.initPiece(roomId);
    }

    @PatchMapping("/{roomId}/pieces")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void movePiece(@PathVariable final int roomId,
                          @RequestBody final MoveRequestDto moveRequestDto) {
        chessService.move(roomId, moveRequestDto);
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
}
