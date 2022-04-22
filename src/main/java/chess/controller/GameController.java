package chess.controller;

import chess.domain.event.MoveRoute;
import chess.domain.event.MoveEvent;
import chess.dto.CreateGameDto;
import chess.dto.GameDto;
import chess.service.ChessService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/game")
public class GameController {

    private static final String HTML_TEMPLATE_PATH = "game";
    private static final String RESPONSE_MODEL_KEY = "response";

    private final ChessService chessService;

    public GameController(ChessService chessService) {
        this.chessService = chessService;
    }

    @PostMapping("/new")
    public CreateGameDto createGame() {
        return chessService.initGame();
    }

    @GetMapping("/{id}")
    public ModelAndView findGame(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(HTML_TEMPLATE_PATH);
        GameDto gameDto = chessService.findGame(id);
        modelAndView.addObject(RESPONSE_MODEL_KEY, gameDto);
        return modelAndView;
    }

    @PostMapping("/{id}")
    private ModelAndView playGame(@PathVariable int id,
                                  @RequestBody MoveRoute moveRoute) {
        chessService.playGame(id, new MoveEvent(moveRoute));

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(HTML_TEMPLATE_PATH);
        GameDto gameDto = chessService.findGame(id);
        modelAndView.addObject(RESPONSE_MODEL_KEY, gameDto);
        return modelAndView;
    }
}
