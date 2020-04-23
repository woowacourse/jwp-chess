package wooteco.chess.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import wooteco.chess.domain.piece.Team;
import wooteco.chess.service.GameService;
import wooteco.chess.view.dto.requestdto.PositionRequestDTO;
import wooteco.chess.view.dto.responsedto.BoardDTO;
import wooteco.chess.view.dto.responsedto.GameDTO;
import wooteco.chess.view.dto.responsedto.ScoreDTO;

@Controller
public class SpringChessController {
	private final GameService gameService;

	public SpringChessController(GameService gameService) {
		this.gameService = gameService;
	}

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/chess/state")
	public ResponseEntity<GameDTO> findCurrentState() {
		return ResponseEntity.ok(gameService.getCurrentState());
	}

	@PostMapping("/chess/state")
	public ResponseEntity<String> changeState(@RequestBody String request) {
		gameService.changeState(request);
		return ResponseEntity.ok("success");
	}

	@GetMapping("/chess/pieces")
	public ResponseEntity<List<BoardDTO>> findAllPiecesOnBoard() {
		return ResponseEntity.ok(gameService.findAllPiecesOnBoard());
	}

	@GetMapping("/chess/record")
	public ResponseEntity<List<ScoreDTO>> calculateScore() {
		return ResponseEntity.ok(gameService.calculateScore());
	}

	@PostMapping("/chess/move")
	public ResponseEntity<List<BoardDTO>> res(@RequestBody PositionRequestDTO requestDTO) {
		gameService.move(requestDTO);
		return ResponseEntity.ok(gameService.findChangedPiecesOnBoard(requestDTO));
	}

	@GetMapping("/chess/isnotfinish")
	public ResponseEntity<Boolean> isNotFinish() {
		return ResponseEntity.ok(gameService.isNotFinish());
	}

	@GetMapping("/chess/result")
	public ResponseEntity<String> findWinner() {
		return ResponseEntity.ok(this.gameService.getWinner());
	}
}
