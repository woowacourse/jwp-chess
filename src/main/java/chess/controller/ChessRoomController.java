package chess.controller;

import chess.service.room.ChessRoomService;
import dto.ChessGameDto;
import dto.RoomDto;
import dto.RoomRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api")
public class ChessRoomController {
    private final ChessRoomService chessRoomService;

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> checkChessRoomException(Exception exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler({MissingRequestCookieException.class})
    public ResponseEntity<String> checkCookieMissing(Exception exception) {
        return ResponseEntity.badRequest().body("로그인을 해 주세요.");
    }

    @Autowired
    public ChessRoomController(final ChessRoomService chessRoomService) {
        this.chessRoomService = chessRoomService;
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<RoomDto>> loadAll() {
        return ResponseEntity.ok().body(chessRoomService.rooms());
    }
    
    @PostMapping("/room")
    public void create(@CookieValue(value = "user") @Valid @RequestBody RoomRequestDto roomRequestDto, BindingResult bindingResult) {
        System.out.println("create : " + roomRequestDto);
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException();
        }
        chessRoomService.create(roomRequestDto);
    }

    @PostMapping("/room/{id}")
    public ResponseEntity<ChessGameDto> enter(@CookieValue(value = "user") String cookie, @RequestBody RoomRequestDto roomRequestDto) {
        return ResponseEntity.ok().body(chessRoomService.enter(roomRequestDto));
    }
}
