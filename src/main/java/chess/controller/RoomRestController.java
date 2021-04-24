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

import javax.servlet.http.HttpServletRequest;
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
    public long createRoom(@RequestBody @Valid final RoomDto roomDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            final String errorMsg = OutputView.getErrorMessage(bindingResult.getFieldErrors());
            throw new IllegalArgumentException(errorMsg);
        }
        final Long roomId = roomService.save(roomDto.getRoomName());
        return roomId;
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity deleteRoom(@PathVariable final long roomId) {
        roomService.delete(roomId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}