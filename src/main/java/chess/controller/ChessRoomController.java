package chess.controller;

import chess.service.room.ChessRoomService;
import dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api")
public class ChessRoomController {
    private final ChessRoomService chessRoomService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public ChessRoomController(final ChessRoomService chessRoomService,
                               final SimpMessagingTemplate simpMessagingTemplate) {
        this.chessRoomService = chessRoomService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<RoomDto>> loadAll() {
        return ResponseEntity.ok().body(chessRoomService.rooms());
    }

    @PostMapping("/rooms")
    public ResponseEntity<RoomCreateResponse> create(@CookieValue(value = "user") @Valid @RequestBody RoomCreateRequest roomCreateRequest,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("방 이름은 2글자 이상 8글자 이하 비밀번호는 4글자 이상 8글자 이하여야 합니다.");
        }

        return ResponseEntity.ok().body(chessRoomService.create(roomCreateRequest));
    }

    @PostMapping("/rooms/{id}/enter")
    public ResponseEntity<RoomDto> enter(@CookieValue(value = "user") String cookie, @RequestBody RoomRequestDto roomRequestDto) {
        RoomDto roomDto = chessRoomService.enter(roomRequestDto);
        return ResponseEntity.ok(roomDto);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity load(@CookieValue(value = "user") String cookie, @PathVariable Long id) {
        RoomLoadResponse RoomLoadResponse = chessRoomService.load(id);
        simpMessagingTemplate.convertAndSend("/topic/room/" + id, RoomLoadResponse);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/rooms/{id}/exit")
    public ResponseEntity exit(@CookieValue(value = "user") String cookie, @RequestBody RoomRequestDto roomRequestDto) {
        ChessGameDto chessGameDto = chessRoomService.exitReturnEndChessGame(roomRequestDto, cookie);
        simpMessagingTemplate.convertAndSend("/topic/game/" + roomRequestDto.getId(), chessGameDto);
        return ResponseEntity.ok().build();
    }
}
