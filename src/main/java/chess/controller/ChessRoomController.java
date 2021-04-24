package chess.controller;

import chess.service.room.ChessRoomService;
import dto.ChessGameDto;
import dto.RoomDto;
import dto.RoomRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api")
public class ChessRoomController {
    private final ChessRoomService chessRoomService;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> checkChessRoomException(Exception exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
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
    public void create(@Valid @RequestBody RoomRequestDto roomRequestDto, BindingResult bindingResult) {
        System.out.println("create : " + roomRequestDto);
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException();
        }
        chessRoomService.create(roomRequestDto);
    }

    @PostMapping("/room/{id}")
    public ResponseEntity<ChessGameDto> enter(@RequestBody RoomRequestDto roomRequestDto) {
        System.out.println("enter : " + roomRequestDto);
        return ResponseEntity.ok().body(chessRoomService.enter(roomRequestDto));
    }
}
