package chess.controller.spring;

import chess.domain.room.Room;
import chess.dto.RoomDTO;
import chess.dto.RoomRegistrationDTO;
import chess.service.spring.RoomService;
import chess.service.spring.UserService;
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
    private final UserService userService;

    public RoomController(RoomService roomService, UserService userService) {
        this.roomService = roomService;
        this.userService = userService;
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
        String sessionPassword = (String) httpSession.getAttribute("password");
        if (!Objects.isNull(sessionPassword)) {
            throw new IllegalStateException("현재 플레이 중인 게임이 존재합니다.");
        }
        roomService.addRoom(roomRegistrationDTO.getName());
        Room room = roomService.findLastAddedRoom();
        int roomId = room.getId();
        String loginPassword = roomRegistrationDTO.getPassword();
        userService.addUser(roomId, loginPassword);
        httpSession.setAttribute("password", loginPassword);
        httpSession.setAttribute("roomId", String.valueOf(roomId));
        RoomDTO roomDTO = RoomDTO.from(room);
        return ResponseEntity.ok().body(roomDTO);
    }
}
