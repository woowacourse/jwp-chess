package chess.controller;

import chess.dto.RoomCreationRequestDto;
import chess.service.RoomService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChessWebController {

    private final RoomService roomService;

    public ChessWebController(final RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/rooms/{roomId}")
    public String room(@PathVariable final int roomId) {
        final boolean roomExist = roomService.isExistRoom(roomId);
        if (!roomExist) {
            return "index";
        }

        return "board";
    }

    @PostMapping("/rooms")
    @ResponseBody
    public ResponseEntity<Object> createRoom(@RequestBody final RoomCreationRequestDto dto) {
        final int roomId = roomService.createRoom(dto);
        return ResponseEntity.created(URI.create("/rooms/" + roomId)).build();
    }
}
