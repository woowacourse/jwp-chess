package chess.controller;

import chess.service.ChessBoardService;
import chess.service.ChessRoomService;
import chess.webdto.dao.RoomDto;
import chess.webdto.view.ChessGameDto;
import chess.webdto.view.MoveRequestDto;
import chess.webdto.view.RoomNameDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/rooms")
public class SpringChessController {
    private final ChessBoardService chessBoardService;
    private final ChessRoomService chessRoomService;

    public SpringChessController(ChessBoardService chessBoardService, ChessRoomService chessRoomService) {
        this.chessBoardService = chessBoardService;
        this.chessRoomService = chessRoomService;
    }

    @GetMapping
    public ResponseEntity<List<RoomDto>> loadRooms() {
        List<RoomDto> roomDtos = chessRoomService.showRooms();
        return ResponseEntity.ok().body(roomDtos);
    }

    @PostMapping
    public ResponseEntity<Void> createRoom(@RequestBody RoomNameDto roomNameDto) {
        long roomId = chessRoomService.createNewRoom(roomNameDto.getRoomName());
        chessBoardService.startNewGame(roomId);
        URI url = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/" + roomId)
                .build().toUri();
        return ResponseEntity.created(url).build();
    }

    @GetMapping(value = "/{roomId}/previous")
    public ResponseEntity<ChessGameDto> loadPrevGame(@PathVariable long roomId) {
        ChessGameDto chessGameDto = chessBoardService.loadPreviousGame(roomId);
        return ResponseEntity.ok().body(chessGameDto);
    }

    @PostMapping(path = "/{roomId}/move")
    public ResponseEntity<ChessGameDto> move(@RequestBody MoveRequestDto moveRequestDto, @PathVariable long roomId) {
        ChessGameDto chessGameDto = chessBoardService.move(moveRequestDto, roomId);
        return ResponseEntity.status(HttpStatus.CREATED).body(chessGameDto);
    }

}
