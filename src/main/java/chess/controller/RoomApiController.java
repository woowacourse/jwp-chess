package chess.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import chess.common.RoomValidator;
import chess.controller.dto.RoomRequestDto;
import chess.controller.model.RoomModel;
import chess.domain.TeamColor;
import chess.domain.room.Room;
import chess.domain.room.User;
import chess.exception.RoomNotFoundException;
import chess.service.RoomService;
import chess.websocket.commander.dto.EnterRoomRequestDto;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("http")
public class RoomApiController {

    private final RoomService roomService;
    private final User user;
    private final RoomValidator roomValidator;

    public RoomApiController(RoomService roomService, User user,
        RoomValidator roomValidator) {
        this.roomService = roomService;
        this.user = user;
        this.roomValidator = roomValidator;
    }

    @InitBinder("roomRequestDto")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(roomValidator);
    }

    @PostMapping("/rooms")
    public ResponseEntity createRoom(@RequestBody @Valid RoomRequestDto room) {
        final Long roomId = roomService
            .createRoom(room.getTitle(), room.isLocked(), room.getPassword(), room.getNickname(), user);

        final URI uri = linkTo(methodOn(RoomApiController.class)
            .findRoom(roomId)).withSelfRel()
            .toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/rooms/{id}")
    public ResponseEntity findRoom(@PathVariable Long id) {
        Room room = roomService.findRoom(id).orElseThrow(RoomNotFoundException::new);
        return ResponseEntity.ok(new RoomModel(room));
    }

    @GetMapping("/rooms")
    public ResponseEntity rooms() {
        final List<RoomModel> rooms = roomService.rooms()
            .stream()
            .map(RoomModel::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok(rooms);
    }

    @DeleteMapping("/rooms/{id}")
    public ResponseEntity deleteRoom(@PathVariable Long id) {
        return null;
    }

    @PutMapping("/rooms/enter/player")
    public ResponseEntity enterRoomAsPlayer(@RequestBody EnterRoomRequestDto enterRoomRequestDto) {
        roomService.enterRoomAsPlayer(enterRoomRequestDto.getRoomId(),
            enterRoomRequestDto.getPassword(), TeamColor.BLACK, enterRoomRequestDto.getNickname(), user);

        final Room room = roomService.findRoom(enterRoomRequestDto.getRoomId())
            .orElseThrow(RoomNotFoundException::new);

        return ResponseEntity.ok(new RoomModel(room));
    }

    @PutMapping("/rooms/enter/participant")
    public ResponseEntity enterRoomAsParticipant(@RequestBody EnterRoomRequestDto enterRoomRequestDto) {
        roomService.enterRoomAsParticipant(enterRoomRequestDto.getRoomId(),
            enterRoomRequestDto.getPassword(), enterRoomRequestDto.getNickname(), user);

        final Room room = roomService.findRoom(enterRoomRequestDto.getRoomId())
            .orElseThrow(RoomNotFoundException::new);

        return ResponseEntity.ok(new RoomModel(room));
    }
}
