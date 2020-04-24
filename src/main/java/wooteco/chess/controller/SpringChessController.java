package wooteco.chess.controller;

import static wooteco.chess.view.response.ResponseStatus.*;

import java.util.function.Supplier;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import wooteco.chess.service.GameService;
import wooteco.chess.view.dto.requestdto.PositionRequestDTO;
import wooteco.chess.view.response.StandardResponse;

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
	public StandardResponse findCurrentState() {
		return makeResponse(() ->
			new StandardResponse(SUCCESS, gameService.getCurrentState()));
	}

	@PostMapping("/chess/state")
	@ResponseBody
	public StandardResponse changeState(@RequestBody String request) {
		return makeResponse(() -> {
			gameService.changeState(request);
			return new StandardResponse(SUCCESS);
		});
	}

	@GetMapping("/chess/pieces")
	@ResponseBody
	public StandardResponse findAllPiecesOnBoard() {
		return makeResponse(() ->
			new StandardResponse(SUCCESS, gameService.findAllPiecesOnBoard()));
	}

	@GetMapping("/chess/record")
	@ResponseBody
	public StandardResponse calculateScore() {
		return makeResponse(() ->
			new StandardResponse(SUCCESS, gameService.calculateScore()));
	}

	@PostMapping("/chess/move")
	@ResponseBody
	public StandardResponse move(@RequestBody PositionRequestDTO requestDTO) {
		return makeResponse(() -> {
			gameService.move(requestDTO);
			return new StandardResponse(SUCCESS, gameService.findChangedPiecesOnBoard(requestDTO));
		});
	}

	@GetMapping("/chess/isnotfinish")
	@ResponseBody
	public StandardResponse isNotFinish() {
		return makeResponse(() ->
			new StandardResponse(SUCCESS, gameService.isNotFinish()));
	}

	@GetMapping("/chess/result")
	@ResponseBody
	public StandardResponse findWinner() {
		return makeResponse(() ->
			new StandardResponse(SUCCESS, gameService.getWinner()));
	}

	private StandardResponse makeResponse(Supplier<StandardResponse> responseGenerator) {
		try {
			return responseGenerator.get();
		} catch (RuntimeException e) {
			return new StandardResponse(ERROR, e.getMessage());
		}
	}
}
