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
import javax.validation.constraints.NotNull;
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
        handleBindingResult(bindingResult);
        addCookie(response, "playerId", roomDto.getPlayer1());
        System.out.println("playerId" + roomDto.getPlayer1() );
        return roomService.save(roomDto.getRoomName(), roomDto.getPlayer1());
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
        addCookie(response, "playerId", playerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void addCookie(final HttpServletResponse response, final String name, final String value){
        final Cookie playerIdCookie = new Cookie(name, value);
        playerIdCookie.setMaxAge(60 * 5);
        playerIdCookie.setPath("/");
        response.addCookie(playerIdCookie);
    }

    private void handleBindingResult(final BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            final String errorMsg = OutputView.getErrorMessage(bindingResult.getFieldErrors());
            throw new IllegalArgumentException(errorMsg);
        }
    }
}