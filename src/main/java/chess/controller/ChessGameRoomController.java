package chess.controller;

import chess.controller.dto.response.ChessGameRoomTitleResponse;
import chess.service.ChessGameRoomService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
