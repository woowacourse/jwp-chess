package chess.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public ResponseEntity<List<Room>> getRooms() {
        return ResponseEntity.ok(roomService.findAll());
    }

    @PostMapping
    public ResponseEntity<Void> insert(Room room) {
        final long chessId = roomService.insert(room);
        ResponseCookie cookie = ResponseCookie.from("chessId", String.valueOf(chessId))
                                              .path("/")
                                              .build();

        URI location = URI.create("/chess");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.created(location)
                             .headers(httpHeaders)
                             .build();
    }
}
