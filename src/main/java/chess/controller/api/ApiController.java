package chess.controller.api;

import chess.controller.api.dto.MovePositionReq;
import chess.controller.api.dto.RoomCreateReq;
import chess.entity.Room;
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

    private ChessService chessServiceV2;

    public ApiController(ChessService chessServiceV2) {
        this.chessServiceV2 = chessServiceV2;
    }

    @GetMapping("/room/all")
    public ResponseEntity<RoomAllRes> findAllRoom() {
        final List<Room> rooms = chessServiceV2.findAllRoom();
        return ResponseEntity.ok().body(RoomAllRes.createRoomAllRes(rooms));
    }

    @PostMapping("/room")
    public ResponseEntity<Void> insertRoom(@RequestBody RoomCreateReq roomCreateReq) {
        chessServiceV2.insertRoom(roomCreateReq.getTitle(), roomCreateReq.getPassword());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/room/{roomId}/status")
    public ResponseEntity<StatusRes> selectStatus(@PathVariable Long roomId) {
        final List<Double> status = chessServiceV2.findStatusById(roomId);
        return ResponseEntity.ok().body(StatusRes.createStatsRes(status));
    }

    @PostMapping("/room/{roomId}/board")
    public ResponseEntity<Void> insertBoard(@PathVariable Long roomId) {
        chessServiceV2.insertBoard(roomId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/room/{roomId}/square")
    public ResponseEntity<Void> updateSquares(@PathVariable Long roomId, @RequestBody MovePositionReq movePositionReq) {
        chessServiceV2.updateSquares(roomId, movePositionReq.getFrom(), movePositionReq.getTo());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/room/{roomId}/stateEnd")
    public ResponseEntity<Void> updateStateEnd(@PathVariable Long roomId) {
        chessServiceV2.updateStateEnd(roomId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId, @RequestBody PasswordReq passwordReq) {
        chessServiceV2.deleteRoom(roomId, passwordReq.getPassword());
        return ResponseEntity.ok().build();
    }
}
