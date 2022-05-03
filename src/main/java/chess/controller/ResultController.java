package chess.controller;

import chess.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/result")
public class ResultController {

    private static final String HTML_TEMPLATE_PATH = "result";
    private static final String RESPONSE_MODEL_KEY = "response";

    private final GameService gameService;

    public ResultController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/{id}")
    public ModelAndView findAndRenderResult(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(HTML_TEMPLATE_PATH);
        modelAndView.addObject(RESPONSE_MODEL_KEY, gameService.findGameResult(id));
        return modelAndView;
    }
}
