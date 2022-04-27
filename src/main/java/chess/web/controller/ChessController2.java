package chess.web.controller;

import chess.domain.state.StateType;
import chess.web.dto.ChessResultDto;
import chess.web.dto.CreateGameRequestDto;
import chess.web.dto.DeleteGameRequestDto;
import chess.web.dto.GameResponseDto;
import chess.web.dto.MovePositionsDto;
import chess.web.dto.MoveResultDto;
import chess.web.service.ChessService2;
import java.net.URI;
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
public class ChessController2 {

    private final ChessService2 chessService;

    public ChessController2(ChessService2 chessService) {
        this.chessService = chessService;
    }

    @GetMapping
    public ResponseEntity<List<GameResponseDto>> showIndex() {
        return ResponseEntity.ok(chessService.getAllGame());
    }

    @PostMapping
    public ResponseEntity<?> createGame(@RequestBody CreateGameRequestDto gameRequestDto) {
        int gameId = chessService.newGame(gameRequestDto);
        return ResponseEntity.created(URI.create("/game/" + gameId)).build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteGame(@RequestBody DeleteGameRequestDto deleteGameRequestDto) {
        chessService.deleteGame(deleteGameRequestDto);
        return ResponseEntity.created(URI.create("/")).build();
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<?> showGame(@PathVariable int gameId) {
        if (chessService.getStateType(gameId) == StateType.END) {
            return showResult(gameId);
        }
        return ResponseEntity.ok(chessService.getChessStatus(gameId));
    }

    @PatchMapping("/game/{gameId}/move")
    public ResponseEntity<MoveResultDto> move(@PathVariable int gameId,
                                              @RequestBody MovePositionsDto movePositionsDto) {
        return ResponseEntity.ok(chessService.getMoveResult(gameId, movePositionsDto));
    }

    @GetMapping("/game/{gameId}/result")
    public ResponseEntity<ChessResultDto> showResult(@PathVariable int gameId) {
        return ResponseEntity.ok(chessService.getChessResult(gameId));
    }

    @GetMapping("/game/{gameId}/restart")
    public ResponseEntity<?> restartGame(@PathVariable int gameId) {
        chessService.start(gameId);
        return ResponseEntity.created(URI.create("/game/" + gameId)).build();
    }
}
