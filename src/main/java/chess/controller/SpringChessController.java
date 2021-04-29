package chess.controller;

import chess.service.ChessBoardService;
import chess.service.ChessRoomService;
import chess.webdto.dao.RoomDto;
import chess.webdto.view.ChessGameDto;
import chess.webdto.view.MoveRequestDto;
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
    public List<RoomDto> loadRooms() {
        return chessRoomService.showRooms();
    }

    @PostMapping
    public long createRoom(@RequestBody RoomNameDto roomNameDto) {
        return chessRoomService.createNewRoom(roomNameDto.getRoomName());
    }
    @PostMapping("/{roomId}")
    public ChessGameDto startNewGame(@PathVariable long roomId) {
        return chessBoardService.startNewGame(roomId);
    }

    @GetMapping(value = "/{roomId}/previous")
    public ChessGameDto loadPrevGame(@PathVariable long roomId) {
        return chessBoardService.loadPreviousGame(roomId);
    }

    @PostMapping(path = "/{roomId}/move")
    public ChessGameDto move(@RequestBody MoveRequestDto moveRequestDto, @PathVariable long roomId) {
        return chessBoardService.move(moveRequestDto, roomId);
    }

}
