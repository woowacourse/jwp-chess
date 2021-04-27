package chess.controller.spring;

import chess.controller.spring.vo.Pagination;
import chess.controller.spring.vo.SessionVO;
import chess.dto.room.RoomDTO;
import chess.dto.room.RoomRegistrationDTO;
import chess.dto.room.RoomResponseDTO;
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
    public ResponseEntity<RoomResponseDTO> showRooms(@RequestParam(required = false, defaultValue = "1") int pageIndex) {
        int roomCounts = roomService.calculateRoomCounts();
        Pagination pagination = new Pagination(pageIndex, roomCounts);
        List<RoomDTO> roomDtos = roomService.findByLimit(pagination.getOffset(), pagination.getContentCountsPerPage())
                .stream()
                .map(RoomDTO::from)
                .collect(Collectors.toList());
        RoomResponseDTO roomResponseDto = new RoomResponseDTO(roomDtos, pagination);
        return ResponseEntity.ok().body(roomResponseDto);
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
