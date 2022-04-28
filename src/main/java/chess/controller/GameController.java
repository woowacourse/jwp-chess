package chess.controller;

import chess.dto.*;
import chess.service.ChessGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class GameController {

    private final ChessGameService chessGameService;

    public GameController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @PostMapping("/game/create")
    public ResponseEntity<RoomInfoDto> createNewGame(@RequestBody NewRoomInfo newChessGameInfo) {
        final long roomId = chessGameService.createNewRoom(newChessGameInfo);
        final RoomInfoDto roomIdDto = new RoomInfoDto(roomId);
        return ResponseEntity.ok(roomIdDto);
    }

    @GetMapping("/status/{roomId}")
    public ResponseEntity<StatusDto> findStatusByGameName(@PathVariable long roomId) {
        final StatusDto status = chessGameService.findStatus(roomId);
        return ResponseEntity.ok(status);
    }

    @DeleteMapping("/game/{roomId}")
    public ResponseEntity<StatusDto> deleteGame(@PathVariable long roomId) {
        final StatusDto status = chessGameService.deleteRoom(roomId);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/game/load")
    public ResponseEntity<ChessGameDto> loadGame(HttpSession session) {
        long roomId = (long) session.getAttribute("roomId");
        final ChessGameDto loadChessGame = chessGameService.loadRoom(roomId);
        return ResponseEntity.ok(loadChessGame);
    }

    @PutMapping("/move")
    public ResponseEntity<ChessGameDto> move(@RequestBody MovePositionDto movePositionDto, HttpSession session) {
        final long roomId = (long) session.getAttribute("roomId");
        final String currentPosition = movePositionDto.getCurrent();
        final String destinationPosition = movePositionDto.getDestination();
        final ChessGameDto chessGame = chessGameService.move(roomId, currentPosition, destinationPosition);
        return ResponseEntity.ok(chessGame);
    }
}
