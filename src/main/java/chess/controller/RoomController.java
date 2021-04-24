package chess.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import chess.domain.chess.Chess;
import chess.domain.chess.ChessDto;
import chess.domain.room.Room;
import chess.service.RoomService;

@Controller
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public ResponseEntity<List<Room>> listRooms() {
        return ResponseEntity.ok(roomService.findAll());
    }

    @GetMapping("/{roomId}/chess")
    public ResponseEntity<ChessDto> enterRoom(@PathVariable long roomId) {
        long chessId = roomService.findChessIdById(roomId);
        Chess chess = roomService.findChessById(chessId);
        ChessDto chessDto = new ChessDto(chess);

        HttpHeaders httpHeaders = createHeadersWithCookieOf(chessId);
        return ResponseEntity.status(200)
                             .headers(httpHeaders)
                             .body(chessDto);
    }

    @PostMapping
    public ResponseEntity<Void> create(Room room) {
        final long chessId = roomService.insert(room);

        URI location = URI.create("/chess");
        HttpHeaders httpHeaders = createHeadersWithCookieOf(chessId);
        return ResponseEntity.created(location)
                             .headers(httpHeaders)
                             .build();
    }

    private HttpHeaders createHeadersWithCookieOf(long chessId) {
        HttpHeaders headers = new HttpHeaders();
        ResponseCookie cookie = ResponseCookie.from("chessId", String.valueOf(chessId))
                                              .path("/")
                                              .build();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
        return headers;
    }
}
