package chess.controller;

import chess.dto.request.RoomCreateRequest;
import chess.dto.request.RoomEnterRequest;
import chess.dto.request.RoomExitRequest;
import chess.dto.response.ChessRoomStatusResponse;
import chess.dto.response.RoomCreateResponse;
import chess.dto.response.RoomEnterResponse;
import chess.dto.response.RoomListResponse;
import chess.service.room.ChessRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/rooms")
public class ChessRoomController {
    private final ChessRoomService chessRoomService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public ChessRoomController(final ChessRoomService chessRoomService,
                               final SimpMessagingTemplate simpMessagingTemplate) {
        this.chessRoomService = chessRoomService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @GetMapping()
    public ResponseEntity<List<RoomListResponse>> loadAll() {
        return ResponseEntity.ok().body(chessRoomService.rooms());
    }

    @PostMapping()
    public ResponseEntity<RoomCreateResponse> create(@CookieValue(value = "user")
                                                     @Valid @RequestBody RoomCreateRequest request,
                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("방 이름은 2글자 이상 8글자 이하 " +
                    "비밀번호는 4글자 이상 8글자 이하여야 합니다.");
        }

        return ResponseEntity.ok().body(chessRoomService.create(request));
    }

    @PostMapping("/enter")
    public ResponseEntity<RoomEnterResponse> enter(@CookieValue(value = "user") String cookie,
                                         @RequestBody RoomEnterRequest request) {
        RoomEnterResponse roomDto = chessRoomService.enter(request);
        return ResponseEntity.ok(roomDto);
    }

    @GetMapping("/{roomId}")
    @ResponseBody
    public ResponseEntity load(@CookieValue(value = "user") String cookie, @PathVariable Long roomId) {
        ChessRoomStatusResponse ChessRoomStatusResponse = chessRoomService.load(roomId);
        simpMessagingTemplate.convertAndSend("/topic/room/" + roomId, ChessRoomStatusResponse);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/exit")
    public ResponseEntity exit(@CookieValue(value = "user") String cookie,
                               @RequestBody RoomExitRequest request) {
        ChessRoomStatusResponse response = chessRoomService.exitReturnEndChessGame(request);
        simpMessagingTemplate.convertAndSend("/topic/room/" + request.getRoomId(), response);
        return ResponseEntity.ok().build();
    }
}
