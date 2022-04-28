package chess.controller;

import chess.dto.ChessGameDto;
import chess.dto.MovePositionDto;
import chess.dto.NewRoomInfo;
import chess.dto.StatusDto;
import chess.service.ChessGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GameController {

    private final ChessGameService chessGameService;

    public GameController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @PostMapping("/game/create")
    public ResponseEntity<ChessGameDto> createNewGame(@RequestBody NewRoomInfo newChessGameInfo) {
        final ChessGameDto newChessGame = chessGameService.createNewRoom(newChessGameInfo);
        return ResponseEntity.ok(newChessGame);
    }

    @GetMapping("/status")
    public ResponseEntity<StatusDto> findStatusByGameName(@RequestParam("name") String gameName) {
        final StatusDto status = chessGameService.findStatus(gameName);
        return ResponseEntity.ok(status);
    }

    @DeleteMapping("/game")
    public ResponseEntity<StatusDto> deleteGame(@RequestParam("name") String gameName) {
        final StatusDto status = chessGameService.deleteRoom(gameName);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/game")
    public ResponseEntity<ChessGameDto> loadGame(@RequestParam("name") String gameName) {
        final ChessGameDto loadChessGame = chessGameService.loadRoom(gameName);
        return ResponseEntity.ok(loadChessGame);
    }

    @PutMapping("/move")
    public ResponseEntity<ChessGameDto> move(@RequestBody MovePositionDto movePositionDto) {
        final String chessGameName = movePositionDto.getRoomName();
        final String currentPosition = movePositionDto.getCurrent();
        final String destinationPosition = movePositionDto.getDestination();
        final ChessGameDto chessGame = chessGameService.move(chessGameName, currentPosition, destinationPosition);
        return ResponseEntity.ok(chessGame);
    }
}
