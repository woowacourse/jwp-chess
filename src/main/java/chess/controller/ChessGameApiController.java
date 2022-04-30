package chess.controller;

import chess.dto.ChessGameDto;
import chess.dto.ChessGameNameStatusDto;
import chess.dto.CreateGameDto;
import chess.dto.DeleteGameDto;
import chess.dto.MovePositionDto;
import chess.dto.StatusDto;
import chess.service.ChessGameService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChessGameApiController {

    private final ChessGameService chessGameService;

    public ChessGameApiController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/games")
    public ResponseEntity<List<ChessGameNameStatusDto>> findAllChessGame() {
        return ResponseEntity.ok(chessGameService.findAllChessGame());
    }

    @PostMapping("/game")
    public ResponseEntity<ChessGameNameStatusDto> createNewGame(@RequestBody CreateGameDto createGameDto) {
        final ChessGameNameStatusDto newChessGame = chessGameService.createNewChessGame(createGameDto);
        return ResponseEntity.ok(newChessGame);
    }

    @GetMapping("/status/{gameId}")
    public ResponseEntity<StatusDto> findStatusByGameName(@PathVariable int gameId) {
        final StatusDto status = chessGameService.findStatus(gameId);
        return ResponseEntity.ok(status);
    }

    @DeleteMapping("/game")
    public ResponseEntity<Void> finishGame(@RequestBody DeleteGameDto deleteGameDto) {
        chessGameService.deleteGame(deleteGameDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/load/{gameId}")
    public ResponseEntity<ChessGameDto> loadGame(@PathVariable int gameId) {
        final char[][] chessMap = chessGameService.loadChessMap(gameId).getChessMap();
        final ChessGameDto chessGameDto = chessGameService.findGameInfo(gameId);

        return ResponseEntity.ok(new ChessGameDto(chessGameDto.getGameName(), chessMap, chessGameDto.getTurn(),
                chessGameDto.isRunning()));
    }

    @PatchMapping("/move/{gameId}")
    public ResponseEntity<ChessGameDto> move(@PathVariable int gameId, @RequestBody MovePositionDto movePositionDto) {
        final String chessGameName = movePositionDto.getChessGameName();
        final String currentPosition = movePositionDto.getCurrent();
        final String destinationPosition = movePositionDto.getDestination();
        final ChessGameDto chessGame = chessGameService.move(gameId, chessGameName, currentPosition,
                destinationPosition);
        return ResponseEntity.ok(chessGame);
    }
}
