package chess.controller.spring;

import chess.dto.RoomDTO;
import chess.service.spring.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public ResponseEntity<List<RoomDTO>> showRooms() {
        List<RoomDTO> allRooms = roomService.findAllRooms()
                .stream()
                .map(RoomDTO::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(allRooms);
    }
}
