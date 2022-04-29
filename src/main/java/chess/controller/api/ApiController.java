package chess.controller.api;

import chess.controller.api.dto.RoomCreateReq;
import chess.service.ChessServiceV2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

   private ChessServiceV2 chessServiceV2;

    public ApiController(ChessServiceV2 chessServiceV2) {
        this.chessServiceV2 = chessServiceV2;
    }

    @PostMapping("/room")
    public ResponseEntity<Void> insertRoom(@RequestBody RoomCreateReq roomCreateReq) {
        chessServiceV2.insertRoom(roomCreateReq.getTitle(), roomCreateReq.getPassword());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/room/{roomId}/board")
    public ResponseEntity<Void> insertBoard(@PathVariable Long roomId) {
        chessServiceV2.insertBoard(roomId);
        return ResponseEntity.ok().build();
    }
}
