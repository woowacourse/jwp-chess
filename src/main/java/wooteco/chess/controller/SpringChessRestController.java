package wooteco.chess.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

	@GetMapping("/state")
	public ResponseDto findCurrentState() {
		return gameService.getCurrentState();
	}

	@PostMapping("/state")
	public ResponseDto changeState(@RequestBody String request) {
		return gameService.changeState(request);
	}

	@GetMapping("/pieces")
	public ResponseDto findAllPiecesOnBoard() {
		return gameService.findAllPiecesOnBoard();
	}

	@GetMapping("/record")
	public ResponseDto calculateScore() {
		return gameService.calculateScore();
	}

	@PostMapping("/move")
	public ResponseDto move(@RequestBody PositionRequestDto requestDTO) {
		return gameService.move(requestDTO);
	}

	@GetMapping("/isnotfinish")
	public ResponseDto isNotFinish() {
		return gameService.isNotFinish();
	}

	@GetMapping("/result")
	public ResponseDto findWinner() {
		return gameService.getWinner();
	}
}
