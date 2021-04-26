package chess.controller.spring;

import chess.controller.spring.vo.SessionVO;
import chess.dto.RoomDTO;
import chess.dto.RoomRegistrationDTO;
import chess.service.spring.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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
        List<RoomDTO> roomDTOS = roomService.findAll()
                .stream()
                .map(RoomDTO::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(roomDTOS);
    }

    @PostMapping
    public ResponseEntity<RoomDTO> addRoom(@RequestBody @Valid RoomRegistrationDTO roomRegistrationDTO, HttpSession httpSession) {
        validateUserSession(httpSession);
        String name = roomRegistrationDTO.getName();
        String password = roomRegistrationDTO.getPassword();
        int roomId = roomService.addRoom(name, password);
        httpSession.setAttribute("session", new SessionVO(roomId, password));
        RoomDTO roomDTO = new RoomDTO(roomId, name);
        return ResponseEntity.ok().body(roomDTO);
    }

    private void validateUserSession(HttpSession httpSession) {
        SessionVO sessionVO = (SessionVO) httpSession.getAttribute("session");
        if (!Objects.isNull(sessionVO)) {
            throw new IllegalStateException("현재 플레이 중인 게임이 존재합니다.");
        }
    }
}
