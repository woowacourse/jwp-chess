package chess.controller;

import chess.dto.view.GameCountDto;
import chess.service.GameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeController {

    private static final String HTML_TEMPLATE_PATH = "home";
    private static final String RESPONSE_MODEL_KEY = "response";

    private final GameService gameService;

    public HomeController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public ModelAndView renderHome() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(HTML_TEMPLATE_PATH);
        GameCountDto gameCountDto = gameService.countGames();
        modelAndView.addObject(RESPONSE_MODEL_KEY, gameCountDto);
        return modelAndView;
    }
}
