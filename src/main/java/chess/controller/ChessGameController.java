package chess.controller;

import chess.controller.dto.request.ChessGamePasswordRequest;
import chess.controller.dto.request.ChessGameRequest;
import chess.controller.dto.request.PieceMoveRequest;
import chess.controller.dto.request.PromotionRequest;
import chess.controller.dto.response.ChessGameResponse;
import chess.controller.dto.response.ChessGameScoreResponse;
import chess.controller.dto.response.ChessGameStatusResponse;
import chess.controller.dto.response.ChessGameWinnerResponse;
import chess.controller.dto.response.PieceResponse;
import chess.service.ChessGameService;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
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
@RequestMapping("/chessgames")
public class ChessGameController {

    private final ChessGameService chessGameService;

    public ChessGameController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @PostMapping
    public ResponseEntity<Long> createNewGame(@RequestBody ChessGameRequest chessGameRequest) {
        long chessGameId = chessGameService.createNewChessGame(chessGameRequest.getTitle(),
                chessGameRequest.getPassword());
        return ResponseEntity.created(URI.create("/chessgames/" + chessGameId)).build();
    }

    @GetMapping
    public ResponseEntity<List<ChessGameResponse>> findAllChessGame() {
        List<ChessGameResponse> chessGameResponses = chessGameService.findAllChessGame().stream()
                .map(ChessGameResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(chessGameResponses);
    }

    @PostMapping("/{chessGameId}")
    public ResponseEntity<List<PieceResponse>> loadChessGame(@PathVariable long chessGameId,
                                                             @RequestBody ChessGamePasswordRequest chessGamePasswordRequest) {
        List<PieceResponse> pieceResponses = chessGameService.findChessBoard(chessGameId,
                        chessGamePasswordRequest.getPassword())
                .entrySet()
                .stream()
                .map(PieceResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pieceResponses);
    }

    @DeleteMapping("/{chessGameId}")
    public ResponseEntity<Void> deleteChessGame(@PathVariable long chessGameId,
                                                @RequestBody ChessGamePasswordRequest chessGamePasswordRequest) {
        chessGameService.deleteChessGame(chessGameId, chessGamePasswordRequest.getPassword());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{chessGameId}/move")
    public ResponseEntity<Void> movePiece(@PathVariable long chessGameId,
                                          @RequestBody PieceMoveRequest pieceMoveRequest) {
        chessGameService.move(chessGameId, pieceMoveRequest.toSourcePosition(), pieceMoveRequest.toTargetPosition());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{chessGameId}/promotion")
    public ResponseEntity<Void> promotionPiece(@PathVariable long chessGameId,
                                               @RequestBody PromotionRequest promotionRequest) {
        chessGameService.promotion(chessGameId, promotionRequest.toPromotionPiece());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{chessGameId}/end")
    public ResponseEntity<Void> endChessGame(@PathVariable long chessGameId) {
        chessGameService.endChessGame(chessGameId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{chessGameId}/score")
    public ResponseEntity<List<ChessGameScoreResponse>> calculateScore(@PathVariable long chessGameId) {
        List<ChessGameScoreResponse> chessGameScoreResponses = chessGameService.currentScore(chessGameId)
                .entrySet()
                .stream()
                .map(ChessGameScoreResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(chessGameScoreResponses);
    }

    @GetMapping("/{chessGameId}/status")
    public ResponseEntity<ChessGameStatusResponse> chessGameStatus(@PathVariable long chessGameId) {
        return ResponseEntity.ok(new ChessGameStatusResponse(chessGameService.isEndGame(chessGameId)));
    }

    @GetMapping("/{chessGameId}/winner")
    public ResponseEntity<ChessGameWinnerResponse> chessGameWinner(@PathVariable long chessGameId) {
        return ResponseEntity.ok(ChessGameWinnerResponse.from(chessGameService.winner(chessGameId)));
    }
}
