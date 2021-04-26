package chess.controller;

import chess.dto.RoomDto;
import chess.service.CookieHandler;
import chess.service.PlayerService;
import chess.service.RoomService;
import chess.view.OutputView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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

    @PostMapping("/create")
    public long createRoom(@RequestBody @Valid final RoomDto room,
                           final BindingResult bindingResult,
                           final HttpServletResponse response) {
        handleBindingResult(bindingResult);
        final long roomId = roomService.save(room.getRoomName(), room.getPlayer1());
        cookieHandler.addPlayerIdCookie(response, roomId, room.getPlayer1());
        return roomId;
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity deleteRoom(@PathVariable long roomId,
                                     final HttpServletRequest request,
                                     final HttpServletResponse response) {
        cookieHandler.remove(roomId, request, response);
        roomService.delete(roomId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/enter/{roomId}")
    public ResponseEntity enterRoom(@PathVariable final long roomId,
                                    @RequestBody final String playerId,
                                    final HttpServletResponse response) {
        playerService.enter(roomId, playerId);
        cookieHandler.addPlayerIdCookie(response, roomId, playerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void handleBindingResult(final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            final String errorMsg = OutputView.getErrorMessage(bindingResult.getFieldErrors());
            throw new IllegalArgumentException(errorMsg);
        }
    }
}