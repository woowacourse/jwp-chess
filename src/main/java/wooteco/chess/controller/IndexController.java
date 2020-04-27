package wooteco.chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import wooteco.chess.service.ChessGameService;

@Controller
public class IndexController {
	private final ChessGameService chessGameService;

	public IndexController(ChessGameService chessGameService) {
		this.chessGameService = chessGameService;
	}

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/rooms/{id}")
	public String renderRoom(@PathVariable Integer id, Model model) {
		try {
			chessGameService.find(id);
			model.addAttribute("id", id);
			return "game";
		} catch (Exception e) {
			return "index";
		}
	}
}
