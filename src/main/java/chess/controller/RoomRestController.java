package chess.controller;

import chess.controller.dto.RoomDto;
import chess.controller.dto.RoomInfoDto;
import chess.service.RoomService;
import chess.view.OutputView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;

@RestController
@RequestMapping("/room")
public class RoomRestController {
    private final RoomService roomService;

    public RoomRestController(final RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/create")
    public RedirectView createRoom(@Valid final RoomDto roomDto, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            final String errorMsg = OutputView.getErrorMessage(bindingResult.getFieldErrors());
            throw new IllegalArgumentException(errorMsg);
        }

        final Long roomId = roomService.save(roomDto.getRoomName());
        return new RedirectView("/game/load/" + roomId);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity deleteRoom(@PathVariable final long roomId) {
        roomService.delete(roomId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}