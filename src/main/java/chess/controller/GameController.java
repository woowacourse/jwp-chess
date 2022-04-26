package chess.controller;

import chess.domain.auth.EncryptedAuthCredentials;
import chess.domain.board.piece.Color;
import chess.domain.event.MoveEvent;
import chess.domain.event.MoveRoute;
import chess.dto.response.SearchResultDto;
import chess.service.ChessService;
import chess.util.CookieUtils;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CookieValue;
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
    public int createGame(EncryptedAuthCredentials authCredentials,
                          HttpServletResponse response) {
        int newGameId = chessService.initGame(authCredentials);
        response.addCookie(CookieUtils.generate(newGameId, Color.WHITE));
        return newGameId;
    }

    @GetMapping("/{id}")
    public ModelAndView findAndRenderGame(@PathVariable int id) {
        return getGameModelAndView(id);
    }

    @PostMapping("/{id}")
    public void enterGame(@PathVariable int id,
                          EncryptedAuthCredentials authCredentials,
                          HttpServletResponse response) {
        // TODO: create Opponent(BLACK) or validate Player(WHITE/BLACK) password
        response.addCookie(CookieUtils.generate(id, Color.BLACK));
    }

    @PutMapping("/{id}")
    public ModelAndView updateGame(@PathVariable int id,
                                   @CookieValue(value = CookieUtils.KEY) String cookie,
                                   @RequestBody MoveRoute moveRoute) {
        chessService.playGame(id, new MoveEvent(moveRoute));
        return getGameModelAndView(id);
    }

    @DeleteMapping("/{id}")
    public void deleteGame(@PathVariable int id,
                           EncryptedAuthCredentials authCredentials) {
        chessService.deleteFinishedGame(id, authCredentials);
    }

    @GetMapping("/search")
    public SearchResultDto searchResult(@RequestParam(name = "game_id") int gameId) {
        return chessService.searchGame(gameId);
    }

    private ModelAndView getGameModelAndView(int id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(PLAY_GAME_HTML_TEMPLATE_PATH);
        modelAndView.addObject(RESPONSE_MODEL_KEY, chessService.findGame(id));
        return modelAndView;
    }
}
