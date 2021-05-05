package chess.controller;

import chess.dto.RoomDto;
import chess.service.PlayerService;
import chess.service.RoomService;
import chess.util.CookieHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/room")
public class RoomRestController {
    private final RoomService roomService;
    private final PlayerService playerService;
    private final CookieHandler cookieHandler;

    public RoomRestController(final RoomService roomService,
                              final PlayerService playerService,
                              final CookieHandler cookieHandler) {
        this.roomService = roomService;
        this.playerService = playerService;
        this.cookieHandler = cookieHandler;
    }

    @PostMapping
    public ResponseEntity createRoom(@RequestBody @Valid final RoomDto room,
                                     final HttpServletResponse response) {
        final long roomId = roomService.create(room.getRoomName(), room.getPlayer1());
        cookieHandler.addPlayerIdCookie(response, roomId, room.getPlayer1());
        return ResponseEntity.ok(roomId);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity deleteRoom(@PathVariable Long roomId,
                                     final HttpServletRequest request,
                                     final HttpServletResponse response) {
        cookieHandler.remove(roomId, request, response);
        roomService.delete(roomId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/enter/{roomId}")
    public ResponseEntity enterRoom(@PathVariable final Long roomId,
                                    @RequestBody final String playerId,
                                    final HttpServletResponse response) {
        playerService.enter(roomId, playerId);
        cookieHandler.addPlayerIdCookie(response, roomId, playerId);
        return ResponseEntity.ok(roomId);
    }
}