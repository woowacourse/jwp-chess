package chess.controller;

import chess.controller.dto.request.ChessGameRoomCreateRequest;
import chess.controller.dto.request.ChessGameRoomDeleteRequest;
import chess.service.ChessGameRoomService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chessgames")
public class ChessGameRoomController {

    private final ChessGameRoomService chessGameRoomService;

    public ChessGameRoomController(final ChessGameRoomService chessGameRoomService) {
        this.chessGameRoomService = chessGameRoomService;
    }

    @PostMapping
    public ResponseEntity<Long> createNewGame(@RequestBody ChessGameRoomCreateRequest request) {
        long chessGameId = chessGameRoomService.createNewChessGame(request.toNewChessGameRoom());
        return ResponseEntity.created(URI.create("/chessgames/" + chessGameId)).build();
    }

    @DeleteMapping("/{chessGameId}")
    public ResponseEntity<Void> deleteChessGame(@PathVariable long chessGameId,
                                                @RequestBody ChessGameRoomDeleteRequest request) {
        chessGameRoomService.deleteChessRoom(chessGameId, request);
        return ResponseEntity.noContent().build();
    }
}
