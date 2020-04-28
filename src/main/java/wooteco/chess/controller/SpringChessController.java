package wooteco.chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
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
		model.addAllAttributes(service.getBoard(gameId));
		return "chess";
	}
}
