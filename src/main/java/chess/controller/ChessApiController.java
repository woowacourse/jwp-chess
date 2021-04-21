package chess.controller;

import chess.domain.piece.Position;
import chess.dto.ChessGameResponseDto;
import chess.dto.ChessGameInfoResponseDto;
import chess.dto.ChessGamesSaveDto;
import chess.dto.ScoreDto;
import chess.service.ChessGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequestMapping("/api/chessgames")
@RestController
public class ChessApiController {

    private final ChessGameService chessGameService;

    public ChessApiController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @PutMapping("/{chessGameId}/pieces")
    public ResponseEntity<ChessGameResponseDto> movePiece(@PathVariable("chessGameId") Long chessGameId,
                                                          @RequestParam(value = "source") String sourcePosition,
                                                          @RequestParam(value = "target") String targetPosition) {
        Position source = Position.parseChessPosition(sourcePosition);
        Position target = Position.parseChessPosition(targetPosition);
        ChessGameResponseDto chessGameResponseDto = chessGameService.moveChessPiece(chessGameId, source, target);
        return ResponseEntity.ok(chessGameResponseDto);
    }

    @GetMapping("/{chessGameId}")
    public ResponseEntity<ChessGameResponseDto> findChessGame(@PathVariable("chessGameId") Long id) {
        ChessGameResponseDto latestPlayingGame = chessGameService.findChessGameById(id);
        return ResponseEntity.ok(latestPlayingGame);
    }

    @PostMapping
    public ResponseEntity<ChessGameInfoResponseDto> createNewChessGame(@RequestBody ChessGamesSaveDto chessGamesSaveDto) {
        ChessGameInfoResponseDto newChessGame = chessGameService.createNewChessGame(chessGamesSaveDto.getTitle());
        return ResponseEntity.created(URI.create("/chessgames/" + newChessGame.getChessGameId())).body(newChessGame);
    }

    @PutMapping("/{chessGameId}")
    public ResponseEntity<ChessGameResponseDto> startChessGame(@PathVariable("chessGameId") Long chessGameId) {
        return ResponseEntity.ok(chessGameService.startGame(chessGameId));
    }

    @DeleteMapping("/{chessGameId}")
    public ResponseEntity<ChessGameResponseDto> endChessGame(@PathVariable("chessGameId") Long chessGameId) {
        ChessGameResponseDto chessGameResponseDto = chessGameService.endGame(chessGameId);
        return ResponseEntity.ok(chessGameResponseDto);
    }

    @GetMapping("/{chessGameId}/scores")
    public ResponseEntity<ScoreDto> calculateScores(@PathVariable("chessGameId") Long chessGameId) {
        ScoreDto scoreDto = chessGameService.calculateScores(chessGameId);
        return ResponseEntity.ok(scoreDto);
    }

}
