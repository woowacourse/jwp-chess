package chess.api;

import chess.domain.piece.Position;
import chess.service.SpringChessGameService;
import chess.view.dto.ChessGameDto;
import chess.view.dto.ScoreDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChessApiController {

    private final SpringChessGameService chessGameService;

    public ChessApiController(SpringChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/pieces")
    public ResponseEntity<ChessGameDto> findPieces(@RequestParam(value = "source") String sourcePosition,
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
    public ResponseEntity<ChessGameDto> createNewChessGame() {
        ChessGameDto newChessGame = chessGameService.createNewChessGame();
        return ResponseEntity.ok(newChessGame);
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
