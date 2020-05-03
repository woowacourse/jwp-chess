package wooteco.chess.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class Advice {
	@ExceptionHandler(IllegalArgumentException.class)
	public ModelAndView exceptionHandler(Exception e) {
		Map<String, Object> model = new HashMap<>();
		model.put("error", e.getMessage());
		return new ModelAndView("error", model);
	}
}
