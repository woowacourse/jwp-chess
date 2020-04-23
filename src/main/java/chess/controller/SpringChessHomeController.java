package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpringChessHomeController {
	private static final String PATH = "/chess/home";
	private static final String STATIC_PATH = "/index";

	@GetMapping(PATH)
	public String home() {
		return STATIC_PATH;
	}
}
