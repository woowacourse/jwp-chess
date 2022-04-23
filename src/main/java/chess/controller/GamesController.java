package chess.controller;

import chess.dto.GameCountDto;
import chess.service.ChessService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/games")
public class GamesController {

    private static final String HTML_TEMPLATE_PATH = "games";
    private static final String GAME_COUNT_MODEL_KEY = "gameCount";
    private static final String GAME_LIST_MODEL_KEY = "games";

    private final ChessService chessService;

    public GamesController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping
    public ModelAndView renderGames() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(HTML_TEMPLATE_PATH);

        GameCountDto gameCountDto = chessService.countGames();
        modelAndView.addObject(GAME_COUNT_MODEL_KEY, gameCountDto);
        modelAndView.addObject(GAME_LIST_MODEL_KEY, chessService.findGames());
        return modelAndView;
    }
}
