package chess.web.controller;

import chess.service.ChessService;
import chess.service.dto.BoardDto;
import chess.service.dto.ChessGameDto;
import chess.service.dto.GameRequest;
import chess.service.dto.GameResultDto;
import chess.web.controller.dto.MoveRequest;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game/{gameId}")
public class ApiChessController {

    private final ChessService chessService;

    public ApiChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("/board")
    public ResponseEntity<Void> initBoard(@PathVariable Long gameId) {
        chessService.initGame(gameId);
        return ResponseEntity.created(URI.create("/game/" + gameId + "/board")).build();
    }

    @GetMapping("/board")
    public ResponseEntity<BoardDto> getBoardPieces(@PathVariable Long gameId) {
        return ResponseEntity.ok(chessService.getBoard(gameId));
    }

    @PatchMapping("/move")
    public ResponseEntity<ChessGameDto> move(@PathVariable Long gameId, MoveRequest moveRequest) {
        return ResponseEntity.ok(chessService.move(gameId, moveRequest));
    }

    @PutMapping("/end")
    public ResponseEntity<Void> endGame(@PathVariable Long gameId) {
        chessService.endGame(gameId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteGame(@PathVariable Long gameId, @RequestBody GameRequest gameRequest) {
        chessService.deleteGame(gameId, gameRequest.getPassword());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status")
    public ResponseEntity<GameResultDto> getResult(@PathVariable Long gameId) {
        GameResultDto gameResultDto = chessService.getResult(gameId);
        chessService.endGame(gameId);
        return ResponseEntity.ok(gameResultDto);
    }
}
