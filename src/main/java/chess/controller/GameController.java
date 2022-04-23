package chess.controller;

import chess.domain.auth.AuthCredentials;
import chess.domain.event.MoveEvent;
import chess.domain.event.MoveRoute;
import chess.dto.CreateGameDto;
import chess.dto.GameDto;
import chess.service.ChessService2;
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

    private static final String NEW_HTML_TEMPLATE_PATH = "new";
    private static final String HTML_TEMPLATE_PATH = "game";
    private static final String RESPONSE_MODEL_KEY = "response";

    private final ChessService2 chessService;

    public GameController(ChessService2 chessService) {
        this.chessService = chessService;
    }

    @GetMapping
    public ModelAndView renderNew() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(NEW_HTML_TEMPLATE_PATH);
        return modelAndView;
    }

    @PostMapping
    public CreateGameDto createGame(@RequestBody AuthCredentials authCredentials) {
        return chessService.initGame(authCredentials);
    }

    @GetMapping("/{id}")
    public ModelAndView findAndRenderGame(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(HTML_TEMPLATE_PATH);
        GameDto gameDto = chessService.findGame(id);
        modelAndView.addObject(RESPONSE_MODEL_KEY, gameDto);
        return modelAndView;
    }

    @PutMapping("/{id}")
    private ModelAndView updateGame(@PathVariable int id,
                                    @RequestBody MoveRoute moveRoute) {
        chessService.playGame(id, new MoveEvent(moveRoute));

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(HTML_TEMPLATE_PATH);
        GameDto gameDto = chessService.findGame(id);
        modelAndView.addObject(RESPONSE_MODEL_KEY, gameDto);
        return modelAndView;
    }
}
