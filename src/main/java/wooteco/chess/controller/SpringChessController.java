package wooteco.chess.controller;

import static java.util.stream.Collectors.*;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import wooteco.chess.domain.entity.PieceEntity;
import wooteco.chess.domain.position.Position;
import wooteco.chess.service.SpringChessService;

@Controller
public class SpringChessController {

	private SpringChessService service;

	public SpringChessController(SpringChessService service) {
		this.service = service;
	}

	@GetMapping("/")
	public String renderStart(Model model) {
		model.addAttribute("rooms", service.getGames());
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

	@PutMapping("/api/piece/{game_id}")
	@ResponseBody
	public String updateBoard(@PathVariable String game_id, @RequestBody Map<String, String> moveInfo) {
		String from = moveInfo.get("from");
		String to = moveInfo.get("to");
		service.move(game_id, Position.of(from), Position.of(to));
		return from + " " + to;
	}

	@GetMapping("/status")
	private String renderResult(@RequestParam("game_id") String game_id, Model model) {
		model.addAllAttributes(service.getResult(game_id));
		return "status";
	}

	@ResponseStatus(value = HttpStatus.CONFLICT)
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseBody
	public String handleException(IllegalArgumentException e) {
		return e.getMessage();
	}
}
