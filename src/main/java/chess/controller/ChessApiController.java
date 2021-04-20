package chess.controller;

import chess.domain.piece.Position;
import chess.dto.ChessGameDto;
import chess.dto.ChessGameResponseDto;
import chess.dto.ScoreDto;
import chess.service.ChessGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequestMapping("/chessgames")
@RestController
public class ChessApiController {

    private final ChessGameService chessGameService;

    public ChessApiController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @PutMapping("/{chessGameId}/pieces")
    public ResponseEntity<ChessGameDto> movePiece(@PathVariable("chessGameId") Long chessGameId,
                                                  @RequestParam(value = "source") String sourcePosition,
                                                  @RequestParam(value = "target") String targetPosition) {
        Position source = Position.parseChessPosition(sourcePosition);
        Position target = Position.parseChessPosition(targetPosition);
        ChessGameDto chessGameDto = chessGameService.moveChessPiece(chessGameId, source, target);
        return ResponseEntity.ok(chessGameDto);
    }

    @GetMapping("/{chessGameId}")
    public ResponseEntity<ChessGameDto> findChessGame(@PathVariable("chessGameId") Long id) {
        ChessGameDto latestPlayingGame = chessGameService.findChessGameById(id);
        return ResponseEntity.ok(latestPlayingGame);
    }

    @PostMapping()
    public ResponseEntity<ChessGameResponseDto> createNewChessGame() {
        ChessGameResponseDto newChessGame = chessGameService.createNewChessGame();
        return ResponseEntity.created(URI.create("/chessgames/" + newChessGame.getChessGameId())).body(newChessGame);
    }

    @DeleteMapping("/{chessGameId}")
    public ResponseEntity<ChessGameDto> endChessGame(@PathVariable("chessGameId") Long chessGameId) {
        ChessGameDto chessGameDto = chessGameService.endGame(chessGameId);
        return ResponseEntity.ok(chessGameDto);
    }

    @GetMapping("/{chessGameId}/scores")
    public ResponseEntity<ScoreDto> calculateScores(@PathVariable("chessGameId") Long chessGameId) {
        ScoreDto scoreDto = chessGameService.calculateScores(chessGameId);
        return ResponseEntity.ok(scoreDto);
    }

}
