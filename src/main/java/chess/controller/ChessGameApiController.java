package chess.controller;

import chess.dto.request.CreateGameDto;
import chess.dto.request.DeleteGameDto;
import chess.dto.request.MovePositionDto;
import chess.dto.response.ChessGameDto;
import chess.dto.response.ChessGameStatusDto;
import chess.dto.response.PlayerScoreDto;
import chess.service.ChessGameService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/games")
public class ChessGameApiController {

    private final ChessGameService chessGameService;

    public ChessGameApiController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping
    public ResponseEntity<List<ChessGameStatusDto>> findAllChessGame() {
        return ResponseEntity.ok(chessGameService.findAllChessGame());
    }

    @PostMapping
    public ResponseEntity<ChessGameStatusDto> createNewGame(@RequestBody CreateGameDto createGameDto) {
        final ChessGameStatusDto newChessGame = chessGameService.createNewChessGame(createGameDto);
        return ResponseEntity.ok(newChessGame);
    }

    @GetMapping("/{gameId}/status")
    public ResponseEntity<PlayerScoreDto> findStatusByGameName(@PathVariable int gameId) {
        final PlayerScoreDto status = chessGameService.findStatus(gameId);
        return ResponseEntity.ok(status);
    }

    @DeleteMapping
    public ResponseEntity<Void> finishGame(@RequestBody DeleteGameDto deleteGameDto) {
        chessGameService.deleteGame(deleteGameDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{gameId}/load")
    public ResponseEntity<ChessGameDto> loadGame(@PathVariable int gameId) {
        final char[][] chessMap = chessGameService.loadChessMap(gameId).getChessMap();
        final ChessGameStatusDto info = chessGameService.findGameInfoById(gameId);

        return ResponseEntity.ok(new ChessGameDto(info.getName(), chessMap, info.getTurn(), info.isRunning()));
    }

    @PatchMapping("/{gameId}/move")
    public ResponseEntity<ChessGameDto> move(@PathVariable int gameId, @RequestBody MovePositionDto movePositionDto) {
        final String chessGameName = movePositionDto.getChessGameName();
        final String currentPosition = movePositionDto.getCurrent();
        final String destinationPosition = movePositionDto.getDestination();
        final ChessGameDto chessGame = chessGameService.move(gameId, chessGameName, currentPosition,
                destinationPosition);
        return ResponseEntity.ok(chessGame);
    }
}
