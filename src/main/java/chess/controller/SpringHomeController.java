package chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chess/home")
public class SpringHomeController {

	@GetMapping
	public String home() {
		return "index";
	}
}
