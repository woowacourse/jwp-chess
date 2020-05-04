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
@RequestMapping("/chess")
public class SpringChessRestController {
	private final GameService gameService;

	public SpringChessRestController(GameService gameService) {
		this.gameService = gameService;
	}

	@GetMapping("/games")
	public ResponseDto games() {
		return gameService.findAllGameIds();
	}

	@PostMapping("/game")
	public ResponseDto createNewGame() {
		return gameService.createNewGame();
	}

	@GetMapping("/state/{id}")
	public ResponseDto findCurrentState(@PathVariable String id) {
		return gameService.getCurrentState(id);
	}

	@PutMapping("/state/{id}")
	public ResponseDto changeState(@RequestBody String request, @PathVariable String id) {
		return gameService.changeState(id, request);
	}

	@GetMapping("/pieces/{id}")
	public ResponseDto findAllPiecesOnBoard(@PathVariable String id) {
		return gameService.findAllPiecesOnBoard(id);
	}

	@GetMapping("/record/{id}")
	public ResponseDto calculateScore(@PathVariable String id) {
		return gameService.calculateScore(id);
	}

	@PutMapping("/move/{id}")
	public ResponseDto move(@RequestBody PositionRequestDto requestDTO, @PathVariable String id) {
		return gameService.move(id, requestDTO);
	}

	@GetMapping("/isnotfinish/{id}")
	public ResponseDto isNotFinish(@PathVariable String id) {
		return gameService.isNotFinish(id);
	}

	@GetMapping("/result/{id}")
	public ResponseDto findWinner(@PathVariable String id) {
		return gameService.getWinner(id);
	}
}