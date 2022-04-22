package chess.controller;

import chess.controller.dto.PieceResponse;
import chess.domain.ChessBoard;
import chess.service.ChessGameService;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
		ChessBoard chessBoard = chessGameService.findChessBoard(chessGameId);
		List<PieceResponse> pieceResponses = chessBoard.getPieces()
				.entrySet()
				.stream()
				.map(PieceResponse::from)
				.collect(Collectors.toList());
		return ResponseEntity.ok(pieceResponses);
	}
}
