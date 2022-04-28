package chess.controller;

import chess.domain.auth.EncryptedAuthCredentials;
import chess.domain.event.MoveEvent;
import chess.dto.request.MoveRouteDto;
import chess.dto.response.SearchResultDto;
import chess.service.ChessService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public int createGame(EncryptedAuthCredentials authCredentials) {
        return chessService.initGame(authCredentials);
    }

    @GetMapping("/{id}")
    public ModelAndView findAndRenderGame(@PathVariable int id) {
        return getGameModelAndView(id);
    }

    @PutMapping("/{id}")
    public ModelAndView updateGame(@PathVariable int id,
                                   @RequestBody MoveRouteDto moveRoute) {
        chessService.playGame(id, new MoveEvent(moveRoute.toDomain()));
        return getGameModelAndView(id);
    }

    @DeleteMapping("/{id}")
    public void deleteGame(@PathVariable int id,
                           EncryptedAuthCredentials authCredentials) {
        chessService.deleteFinishedGame(id, authCredentials);
    }

    @GetMapping("/search")
    public SearchResultDto searchGame(@RequestParam(name = "game_id") int gameId) {
        return chessService.searchGame(gameId);
    }

    private ModelAndView getGameModelAndView(int id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(PLAY_GAME_HTML_TEMPLATE_PATH);
        modelAndView.addObject(RESPONSE_MODEL_KEY, chessService.findGame(id));
        return modelAndView;
    }
}
