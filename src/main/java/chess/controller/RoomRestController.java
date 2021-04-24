package chess.controller;

import chess.controller.dto.RoomDto;
import chess.controller.dto.RoomInfoDto;
import chess.service.RoomService;
import chess.view.OutputView;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/room")
public class RoomRestController {
    private final RoomService roomService;

    public RoomRestController(final RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/create")
    public long createRoom(@RequestBody @Valid final RoomDto roomDto,
                           final BindingResult bindingResult,
                           final HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            final String errorMsg = OutputView.getErrorMessage(bindingResult.getFieldErrors());
            throw new IllegalArgumentException(errorMsg);
        }

        final Long roomId = roomService.save(roomDto.getRoomName(), roomDto.getPlayer1());
        addCookie(response, "playerId", roomId);
        return roomId;
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity deleteRoom(@PathVariable final long roomId) {
        roomService.delete(roomId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/enter/{roomId}")
    public ResponseEntity enterRoom(@PathVariable final long roomId,
                                    @RequestBody final String playerId,
                                    final HttpServletResponse response){
        roomService.enter(roomId, playerId);
        addCookie(response, "playerId", roomId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void addCookie(final HttpServletResponse response, final String name, final Object value){
        final Cookie playerIdCookie = new Cookie(name, (String) value);
        playerIdCookie.setMaxAge(60 * 5);
        response.addCookie(playerIdCookie);
    }
}