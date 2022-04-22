package chess.controller;

import chess.dto.GameCountDto;
import chess.service.ChessService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeController {

    private static final String HTML_TEMPLATE_PATH = "home";
    private static final String RESPONSE_MODEL_KEY = "response";

    private final ChessService chessService;

    public HomeController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(HTML_TEMPLATE_PATH);
        GameCountDto gameCountDto = chessService.countGames();
        modelAndView.addObject(RESPONSE_MODEL_KEY, gameCountDto);
        return modelAndView;
    }
}
