package chess.controller;

import chess.controller.dto.request.ChessGameRoomCreateRequest;
import chess.controller.dto.request.ChessGameRoomDeleteRequest;
import chess.controller.dto.response.ChessGameRoomTitleResponse;
import chess.service.ChessGameRoomService;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping
    public ResponseEntity<List<ChessGameRoomTitleResponse>> findAllChessGameRooms() {
        return ResponseEntity.ok(chessGameRoomService.findAllChessGameRooms()
                .stream()
                .map(ChessGameRoomTitleResponse::from)
                .collect(Collectors.toList()));
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
