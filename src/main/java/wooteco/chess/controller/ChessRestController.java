package wooteco.chess.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.chess.service.GameService;
import wooteco.chess.view.dto.requestdto.PositionRequestDto;
import wooteco.chess.view.response.ResponseDto;

@RestController
@RequestMapping("/games")
public class ChessRestController {
	private final GameService gameService;

	public ChessRestController(GameService gameService) {
		this.gameService = gameService;
	}

	@GetMapping
	public ResponseDto games() {
		return gameService.findAllGameIds();
	}

	@PostMapping
	public ResponseDto createNewGame() {
		return gameService.createNewGame();
	}

	@GetMapping("/{id}/state")
	public ResponseDto findCurrentState(@PathVariable String id) {
		return gameService.getCurrentState(id);
	}

	@PutMapping("/{id}/state")
	public ResponseDto changeState(@RequestBody String request, @PathVariable String id) {
		return gameService.changeState(id, request);
	}

	@GetMapping("/{id}/board/pieces")
	public ResponseDto findAllPiecesOnBoard(@PathVariable String id) {
		return gameService.findAllPiecesOnBoard(id);
	}

	@GetMapping("/{id}/scores")
	public ResponseDto calculateScore(@PathVariable String id) {
		return gameService.calculateScore(id);
	}

	@PutMapping("/{id}/board/pieces")
	public ResponseDto move(@RequestBody PositionRequestDto requestDTO, @PathVariable String id) {
		return gameService.move(id, requestDTO);
	}

	@GetMapping("/{id}/non-finish-status")
	public ResponseDto isNotFinish(@PathVariable String id) {
		return gameService.isNotFinish(id);
	}

	@GetMapping("/{id}/winner")
	public ResponseDto findWinner(@PathVariable String id) {
		return gameService.getWinner(id);
	}
}
