package chess.controller;

import chess.domain.auth.EncryptedAuthCredentials;
import chess.domain.auth.PlayerCookie;
import chess.domain.event.MoveEvent;
import chess.dto.request.MoveRouteDto;
import chess.dto.response.CreatedGameDto;
import chess.dto.response.EnterGameDto;
import chess.dto.response.SearchResultDto;
import chess.service.AuthService;
import chess.service.ChessService;
import java.net.URI;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    private final AuthService authService;

    public GameController(ChessService chessService, AuthService authService) {
        this.chessService = chessService;
        this.authService = authService;
    }

    @GetMapping
    public ModelAndView renderNew() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(INIT_GAME_HTML_TEMPLATE_PATH);
        return modelAndView;
    }

    @PostMapping
    public ResponseEntity<Integer> createGame(EncryptedAuthCredentials authCredentials,
                                              HttpServletResponse response) {
        CreatedGameDto createdGame = chessService.initGame(authCredentials);
        response.addCookie(createdGame.getCookie());
        int gameId = createdGame.getGameId();
        URI location = URI.create("/game/" + gameId);
        return ResponseEntity.created(location).body(gameId);
    }

    @GetMapping("/{id}")
    public ModelAndView findAndRenderGame(@PathVariable int id) {
        return getGameModelAndView(id);
    }

    @PutMapping("/{id}")
    public ModelAndView updateGame(@PathVariable int id,
                                   PlayerCookie cookie,
                                   @RequestBody MoveRouteDto moveRoute) {
        chessService.playGame(id, new MoveEvent(moveRoute.toDomain()), cookie);
        return getGameModelAndView(id);
    }

    @DeleteMapping("/{id}")
    public void deleteGame(@PathVariable int id,
                           EncryptedAuthCredentials authCredentials) {
        chessService.deleteFinishedGame(id, authCredentials);
    }

    @GetMapping("/{id}/info")
    public SearchResultDto searchGame(@PathVariable int id) {
        return chessService.searchGame(id);
    }

    @PostMapping("/{id}/auth")
    public ResponseEntity<String> enterGame(@PathVariable int id,
                                            EncryptedAuthCredentials authCredentials,
                                            HttpServletResponse response) {
        EnterGameDto enterGameDto = authService.loginOrSignUpAsOpponent(id, authCredentials);
        response.addCookie(enterGameDto.getCookie());
        return ResponseEntity.ok(enterGameDto.getMessage());
    }

    private ModelAndView getGameModelAndView(int id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(PLAY_GAME_HTML_TEMPLATE_PATH);
        modelAndView.addObject(RESPONSE_MODEL_KEY, chessService.findGame(id));
        return modelAndView;
    }
}
