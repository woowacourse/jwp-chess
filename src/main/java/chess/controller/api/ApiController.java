package chess.controller.api;

import chess.dto.MovePositionRequest;
import chess.dto.PasswordRequest;
import chess.dto.RoomsResponse;
import chess.dto.RoomCreateRequest;
import chess.dto.RoomResponse;
import chess.dto.StatusResponse;
import chess.dto.WinnerRes;
import chess.service.ChessService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final ChessService chessService;

    public ApiController(ChessService chessServiceV2) {
        this.chessService = chessServiceV2;
    }

    @GetMapping("/rooms")
    public ResponseEntity<RoomsResponse> findAllRoom() {
        final List<RoomResponse> rooms = chessService.findAllRoom();
        return ResponseEntity.ok().body(RoomsResponse.createRoomsResponse(rooms));
    }

    @PostMapping("/room")
    public ResponseEntity<Void> insertRoom(@RequestBody RoomCreateRequest roomCreateRequest) {
        chessService.insertRoom(roomCreateRequest.getTitle(), roomCreateRequest.getPassword());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/room/{roomId}/status")
    public ResponseEntity<StatusResponse> selectStatus(@PathVariable Long roomId) {
        final List<Double> status = chessService.findStatusById(roomId);
        return ResponseEntity.ok().body(StatusResponse.createStatsResponse(status));
    }

    @PostMapping("/room/{roomId}/board")
    public ResponseEntity<Void> insertBoard(@PathVariable Long roomId) {
        chessService.insertBoard(roomId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/room/{roomId}/square")
    public ResponseEntity<Void> updateSquares(@PathVariable Long roomId,
                                              @RequestBody MovePositionRequest movePositionRequest) {
        chessService.updateSquares(roomId, movePositionRequest.getFrom(), movePositionRequest.getTo());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/room/{roomId}/state/end")
    public ResponseEntity<Void> updateStateEnd(@PathVariable Long roomId) {
        chessService.updateStateEnd(roomId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/room/{roomId}/state/end")
    public ResponseEntity<WinnerRes> selectWinner(@PathVariable Long roomId) {
        return ResponseEntity.ok().body(chessService.findWinnerById(roomId));
    }

    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId, @RequestBody PasswordRequest passwordRequest) {
        chessService.deleteRoom(roomId, passwordRequest.getPassword());
        return ResponseEntity.ok().build();
    }
}
