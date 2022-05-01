package chess.controller.api;

import chess.dto.MovePositionReq;
import chess.dto.PasswordReq;
import chess.dto.RoomsRes;
import chess.dto.RoomCreateReq;
import chess.dto.RoomRes;
import chess.dto.StatusRes;
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
    public ResponseEntity<RoomsRes> findAllRoom() {
        final List<RoomRes> rooms = chessService.findAllRoom();
        return ResponseEntity.ok().body(RoomsRes.createRoomAllRes(rooms));
    }

    @PostMapping("/room")
    public ResponseEntity<Void> insertRoom(@RequestBody RoomCreateReq roomCreateReq) {
        chessService.insertRoom(roomCreateReq.getTitle(), roomCreateReq.getPassword());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/room/{roomId}/status")
    public ResponseEntity<StatusRes> selectStatus(@PathVariable Long roomId) {
        final List<Double> status = chessService.findStatusById(roomId);
        return ResponseEntity.ok().body(StatusRes.createStatsRes(status));
    }

    @PostMapping("/room/{roomId}/board")
    public ResponseEntity<Void> insertBoard(@PathVariable Long roomId) {
        chessService.insertBoard(roomId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/room/{roomId}/square")
    public ResponseEntity<Void> updateSquares(@PathVariable Long roomId, @RequestBody MovePositionReq movePositionReq) {
        chessService.updateSquares(roomId, movePositionReq.getFrom(), movePositionReq.getTo());
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
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId, @RequestBody PasswordReq passwordReq) {
        chessService.deleteRoom(roomId, passwordReq.getPassword());
        return ResponseEntity.ok().build();
    }
}
