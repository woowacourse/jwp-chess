package chess.controller;

import chess.service.ChessGameService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
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
}
