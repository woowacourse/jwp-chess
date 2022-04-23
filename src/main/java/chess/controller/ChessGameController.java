package chess.controller;

import chess.controller.dto.ChessGameScoreResponse;
import chess.controller.dto.ChessGameStatusResponse;
import chess.controller.dto.ChessGameWinnerResponse;
import chess.controller.dto.PieceMoveRequest;
import chess.controller.dto.PieceResponse;
import chess.controller.dto.PromotionRequest;
import chess.service.ChessGameService;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
	public ResponseEntity<Long> createNewGame() {
		long chessGameId = chessGameService.createNewChessGame();
		return ResponseEntity.created(URI.create("/chessgames/" + chessGameId)).build();
	}

	@GetMapping("/{chessGameId}")
	public ResponseEntity<List<PieceResponse>> loadChessGame(@PathVariable long chessGameId) {
		List<PieceResponse> pieceResponses = chessGameService.findChessBoard(chessGameId)
				.entrySet()
				.stream()
				.map(PieceResponse::from)
				.collect(Collectors.toList());
		return ResponseEntity.ok(pieceResponses);
	}

	@PostMapping("/{chessGameId}/move")
	public ResponseEntity<Void> movePiece(@PathVariable long chessGameId,
										  @RequestBody PieceMoveRequest pieceMoveRequest) {
		chessGameService.move(chessGameId, pieceMoveRequest.toSourcePosition(), pieceMoveRequest.toTargetPosition());
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{chessGameId}/promotion")
	public ResponseEntity<Void> promotionPiece(@PathVariable long chessGameId,
											   @RequestBody PromotionRequest promotionRequest) {
		chessGameService.promotion(chessGameId, promotionRequest.toPromotionPiece());
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
