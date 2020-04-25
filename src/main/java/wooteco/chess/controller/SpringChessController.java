package wooteco.chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import wooteco.chess.service.GameService;
import wooteco.chess.view.dto.requestdto.PositionRequestDto;
import wooteco.chess.view.response.ResponseDto;

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
	@ResponseBody
	public ResponseDto findCurrentState() {
		return gameService.getCurrentState();
	}

	@PostMapping("/chess/state")
	@ResponseBody
	public ResponseDto changeState(@RequestBody String request) {
		return gameService.changeState(request);
	}

	@GetMapping("/chess/pieces")
	@ResponseBody
	public ResponseDto findAllPiecesOnBoard() {
		return gameService.findAllPiecesOnBoard();
	}

	@GetMapping("/chess/record")
	@ResponseBody
	public ResponseDto calculateScore() {
		return gameService.calculateScore();
	}

	@PostMapping("/chess/move")
	@ResponseBody
	public ResponseDto move(@RequestBody PositionRequestDto requestDTO) {
		return gameService.move(requestDTO);
	}

	@GetMapping("/chess/isnotfinish")
	@ResponseBody
	public ResponseDto isNotFinish() {
		return gameService.isNotFinish();
	}

	@GetMapping("/chess/result")
	@ResponseBody
	public ResponseDto findWinner() {
		return gameService.getWinner();
	}
}
