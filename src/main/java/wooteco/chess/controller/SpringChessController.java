package wooteco.chess.controller;

import static java.util.stream.Collectors.*;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.gson.Gson;
import wooteco.chess.domain.entity.PieceEntity;
import wooteco.chess.service.SpringChessService;

@Controller
public class SpringChessController {
	private static final Gson GSON = new Gson();

	private SpringChessService service;

	public SpringChessController(SpringChessService service) {
		this.service = service;
	}

	@GetMapping("/")
	public String renderStart() {
		return "index";
	}

	@GetMapping("/chess")
	public String renderBoard(@RequestParam("game_id") String gameId, Model model) {
		service.initialize(gameId);
		Map<String, String> board = service.getBoard(gameId)
			.stream()
			.collect(toMap(pieceEntity -> pieceEntity.getPosition().getName(), PieceEntity::getSymbol));

		model.addAllAttributes(board);
		return "chess";
	}

	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseBody
	public String handleException(IllegalArgumentException e) {
		return e.getMessage();
	}
}
