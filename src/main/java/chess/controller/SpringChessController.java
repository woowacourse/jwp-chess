package chess.controller;

import chess.service.ChessBoardService;
import chess.service.ChessRoomService;
import chess.webdto.dao.RoomDto;
import chess.webdto.view.ChessGameDto;
import chess.webdto.view.MoveRequestDto;
import chess.webdto.view.RoomNameDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Long> createRoom(@RequestBody RoomNameDto roomNameDto) {
        long newRoom = chessRoomService.createNewRoom(roomNameDto.getRoomName());
        return ResponseEntity.ok().body(newRoom);
    }

    @PostMapping("/{roomId}")
    public ResponseEntity<ChessGameDto> startNewGame(@PathVariable long roomId) {
        ChessGameDto chessGameDto = chessBoardService.startNewGame(roomId);
        return ResponseEntity.ok().body(chessGameDto);
    }

    @GetMapping(value = "/{roomId}/previous")
    public ResponseEntity<ChessGameDto> loadPrevGame(@PathVariable long roomId) {
        ChessGameDto chessGameDto = chessBoardService.loadPreviousGame(roomId);
        return ResponseEntity.ok().body(chessGameDto);
    }

    @PostMapping(path = "/{roomId}/move")
    public ResponseEntity<ChessGameDto> move(@RequestBody MoveRequestDto moveRequestDto, @PathVariable long roomId) {
        ChessGameDto chessGameDto = chessBoardService.move(moveRequestDto, roomId);
        return ResponseEntity.ok().body(chessGameDto);
    }

}
