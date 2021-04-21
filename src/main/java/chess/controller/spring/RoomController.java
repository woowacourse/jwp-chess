package chess.controller.spring;

import chess.domain.room.Room;
import chess.dto.RoomDTO;
import chess.dto.RoomRegistrationDTO;
import chess.service.spring.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;
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
        List<RoomDTO> roomDTOS = roomService.findAllRooms()
                .stream()
                .map(RoomDTO::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(roomDTOS);
    }

    @PostMapping
    public ResponseEntity<RoomDTO> addRoom(@RequestBody RoomRegistrationDTO roomRegistrationDTO, HttpSession httpSession) {
        if (!Objects.isNull(httpSession.getAttribute("password"))) {
            throw new IllegalStateException("현재 플레이 중인 게임이 존재합니다.");
        }
        roomService.addRoom(roomRegistrationDTO.getName());
        Room room = roomService.findLastAddedRoom();
        RoomDTO roomDTO = RoomDTO.from(room);
        httpSession.setAttribute("password", roomRegistrationDTO.getPassword());
        httpSession.setAttribute("roomId", String.valueOf(room.getId()));
        return ResponseEntity.ok().body(roomDTO);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleException(RuntimeException runtimeException) {
        return ResponseEntity.badRequest().body(runtimeException.getMessage());
    }
}
