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

    @Autowired
    public ChessRoomController(final ChessRoomService chessRoomService) {
        this.chessRoomService = chessRoomService;
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<RoomDto>> loadAll() {
        return ResponseEntity.ok().body(chessRoomService.rooms());
    }

    @Transactional
    @PostMapping("/room")
    public void create(@Valid @ModelAttribute RoomRequestDto roomRequestDto, BindingResult bindingResult) {
        chessRoomService.create(roomRequestDto);
    }

    @PostMapping("/room/{id}")
    public ResponseEntity<ChessGameDto> enter(@ModelAttribute RoomRequestDto roomRequestDto) {
        return ResponseEntity.ok().body(chessRoomService.enter(roomRequestDto));
    }
}
