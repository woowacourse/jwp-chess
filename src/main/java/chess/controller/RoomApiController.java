package chess.controller;

import chess.common.RoomValidator;
import chess.controller.model.RoomModel;
import chess.service.RoomService;
import chess.service.RoomServiceNormal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoomApiController {

    private final RoomService roomService;

    public RoomApiController(RoomService roomService) {
        this.roomService = roomService;
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        final RoomValidator roomValidator = new RoomValidator();
        webDataBinder.addValidators(roomValidator);
    }

//    @GetMapping("/rooms/{id}")
//    public ResponseEntity findRoom(@PathVariable Long id) {
//        Room room = roomServiceNormal.roomById(id).orElseThrow(RoomNotFoundException::new);
//        return ResponseEntity.ok(new RoomModel(room));
//    }

    @GetMapping("/rooms")
    public ResponseEntity rooms() {
        final List<RoomModel> rooms = roomService.rooms()
            .stream()
            .map(RoomModel::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok(rooms);
    }

//    @PostMapping("/rooms")
//    public ResponseEntity makeRoom(@RequestBody @Valid RoomRequestDto roomRequestDto) {
//        final Long roomId = roomServiceNormal.createRoom(
//            roomRequestDto.getTitle(),
//            roomRequestDto.isLocked(),
//            roomRequestDto.getPassword(),
//            new User()
//        );
//        URI uri = linkTo(methodOn(RoomApiController.class).findRoom(roomId)).toUri();
//        return ResponseEntity.created(uri).build();
//    }

    @DeleteMapping("/rooms/{id}")
    public ResponseEntity deleteRoom(@PathVariable Long id) {
        return null;
    }
}
