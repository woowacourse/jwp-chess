package wooteco.chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SpringController {
	@GetMapping("/")
	public ModelAndView index() {
		return new ModelAndView("index");
	}
}