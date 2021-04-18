package chess.controller;

import chess.domain.piece.Position;
import chess.dto.ChessGameDto;
import chess.dto.ChessGameResponseDto;
import chess.dto.ScoreDto;
import chess.service.ChessGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class ChessApiController {

    private final ChessGameService chessGameService;

    public ChessApiController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/pieces")
    public ResponseEntity<ChessGameDto> movePiece(@RequestParam(value = "source") String sourcePosition,
                                                   @RequestParam(value = "target") String targetPosition) {
        Position source = Position.parseChessPosition(sourcePosition);
        Position target = Position.parseChessPosition(targetPosition);
        ChessGameDto chessGameDto = chessGameService.moveChessPiece(source, target);
        return ResponseEntity.ok(chessGameDto);
    }

    @GetMapping("/chessgames")
    public ResponseEntity<ChessGameDto> findChessGame() {
        ChessGameDto latestPlayingGame = chessGameService.findLatestPlayingGame();
        return ResponseEntity.ok(latestPlayingGame);
    }

    @PostMapping("/chessgames")
    public ResponseEntity<ChessGameResponseDto> createNewChessGame() {
        ChessGameResponseDto newChessGame = chessGameService.createNewChessGame();
        return ResponseEntity.created(URI.create("/chessgames/" + newChessGame.getChessGameId())).body(newChessGame);
    }

    @DeleteMapping("/chessgames")
    public ResponseEntity<ChessGameDto> endChessGame() {
        ChessGameDto chessGameDto = chessGameService.endGame();
        return ResponseEntity.ok(chessGameDto);
    }

    @GetMapping("/scores")
    public ResponseEntity<ScoreDto> calculateScores() {
        ScoreDto scoreDto = chessGameService.calculateScores();
        return ResponseEntity.ok(scoreDto);
    }

}
