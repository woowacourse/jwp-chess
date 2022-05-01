package chess.controller;

import chess.dto.*;
import chess.service.ChessGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/game")
public class GameController {

    private final ChessGameService chessGameService;

    public GameController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/status/{roomId}")
    public ResponseEntity<StatusDto> findStatusByGameName(@PathVariable long roomId) {
        final StatusDto status = chessGameService.findStatus(roomId);
        return ResponseEntity.ok(status);
    }

    @PostMapping
    public ResponseEntity<RoomInfoDto> createNewGame(@RequestBody NewRoomInfo newChessGameInfo) {
        final long roomId = chessGameService.createNewRoom(newChessGameInfo);
        final RoomInfoDto roomIdDto = new RoomInfoDto(roomId);
        return ResponseEntity.ok(roomIdDto);
    }

    @PutMapping("/move")
    public ResponseEntity<ChessGameDto> move(@RequestBody MovePositionDto movePositionDto) {
        final long roomId = movePositionDto.getRoomId();
        final String currentPosition = movePositionDto.getCurrent();
        final String destinationPosition = movePositionDto.getDestination();
        final ChessGameDto chessGame = chessGameService.move(roomId, currentPosition, destinationPosition);
        return ResponseEntity.ok(chessGame);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteGame(@RequestBody RemoveRoomDto removeRoomDto) {
        chessGameService.deleteRoom(removeRoomDto.getRoomId(), removeRoomDto.getPassword());
        return ResponseEntity.noContent().build();
    }
}
