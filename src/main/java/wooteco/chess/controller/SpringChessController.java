package wooteco.chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class SpringChessController {
	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/game/{id}")
	public String game(@PathVariable String id, Model model) {
		model.addAttribute("id", id);
		return "game";
	}
}