package chess.controller;

import chess.domain.auth.AuthCredentials;
import chess.domain.event.MoveEvent;
import chess.domain.event.MoveRoute;
import chess.dto.CreatedGameDto;
import chess.dto.FullGameDto;
import chess.service.ChessService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/game")
public class GameController {

    private static final String INIT_GAME_HTML_TEMPLATE_PATH = "init_game";
    private static final String PLAY_GAME_HTML_TEMPLATE_PATH = "play_game";
    private static final String RESPONSE_MODEL_KEY = "response";

    private final ChessService chessService;

    public GameController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping
    public ModelAndView renderNew() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(INIT_GAME_HTML_TEMPLATE_PATH);
        return modelAndView;
    }

    @PostMapping
    public CreatedGameDto createGame(@RequestBody AuthCredentials authCredentials) {
        return chessService.initGame(authCredentials);
    }

    @GetMapping("/{id}")
    public ModelAndView findAndRenderGame(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(PLAY_GAME_HTML_TEMPLATE_PATH);
        FullGameDto gameDto = chessService.findGame(id);
        modelAndView.addObject(RESPONSE_MODEL_KEY, gameDto);
        return modelAndView;
    }

    @PutMapping("/{id}")
    public ModelAndView updateGame(@PathVariable int id,
                                   @RequestBody MoveRoute moveRoute) {
        chessService.playGame(id, new MoveEvent(moveRoute));

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(PLAY_GAME_HTML_TEMPLATE_PATH);
        FullGameDto gameDto = chessService.findGame(id);
        modelAndView.addObject(RESPONSE_MODEL_KEY, gameDto);
        return modelAndView;
    }
}
