package chess.controller.spring;

import chess.domain.ChessGame;
import chess.domain.dto.GameDto;
import chess.domain.dto.WebBoardDto;
import chess.domain.web.Game;
import chess.domain.web.GameHistory;
import chess.service.ChessService;
import chess.service.UserService;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spark.utils.StringUtils;

@Controller
public class SpringWebController {
    public static final String REDIRECT = "redirect:";
    private final ChessService chessService;
    private final UserService userService;

    public SpringWebController(ChessService chessService, UserService userService) {
        this.chessService = chessService;
        this.userService = userService;
    }

    @GetMapping("/")
    private String renderLogin() {
        return "login-spring";
    }

    @GetMapping("/games")
    private String renderGames(HttpServletResponse response, Model model,
        @CookieValue("userId") String cookie) {
        Optional<Integer> currentUserId = checkLogin(cookie);
        return currentUserId.map(userId -> games(model, userId))
            .orElseGet(() -> logoutAndRedirect(response));
    }

    private String games(Model model, int userId) {
        List<GameDto> runningGamesByUserId = chessService.findGamesByUserId(userId);
        model.addAttribute("games", runningGamesByUserId);
        model.addAttribute("userName", userService.findUserNameByUserId(userId));
        return "games-spring";
    }

    @GetMapping("/games/{id}")
    private String renderGame(Model model, @PathVariable("id") int gameId,
        HttpServletResponse response,
        @CookieValue("userId") String userCookie,
        @CookieValue(value = "em", required = false) String errorMessageCookie
    ) {
        Optional<Integer> currentUserId = checkLogin(userCookie);
        return currentUserId
            .map(id -> eachGame(model, gameId,
                getErrorMessage(response, gameId, errorMessageCookie)))
            .orElseGet(() -> logoutAndRedirect(response));
    }

    private String eachGame(Model model, int currentGameId, String error) {
        WebBoardDto webBoardDto = chessService.reloadAllHistory(currentGameId).getWebBoardDto();
        model.addAllAttributes(getCurrentGameMap(currentGameId, error, currentGameId, webBoardDto));
        return "chess-spring";
    }

    private String getErrorMessage(HttpServletResponse response, int gameId,
        String errorMessageCookie) {
        Cookie cookie = new Cookie("em", null);
        cookie.setPath("/games/" + gameId);
        response.addCookie(cookie);
        return errorMessageCookie;
    }

    @PostMapping("/login")
    private String login(@RequestParam("userName") String userName, HttpServletResponse response) {
        int userId = userService.addUserIfNotExist(userName);
        Cookie cookie = new Cookie("userId", encodeCookie(String.valueOf(userId)));
        response.addCookie(cookie);
        return REDIRECT +"/games";
    }

    private Optional<Integer> checkLogin(String cookie) {
        String userIdStringFormat = decodeCookie(cookie);
        if (userIdStringFormat == null) {
            return Optional.empty();
        }
        int userId = Integer.parseInt(userIdStringFormat);
        if (userService.isUserExist(userId)) {
            return Optional.of(userId);
        }
        return Optional.empty();
    }

    @PostMapping("/logout")
    private String logoutAndRedirect(HttpServletResponse response) {
        Cookie cookie = new Cookie("user", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return REDIRECT + "/";
    }

    @PostMapping("/start")
    private String start(@CookieValue("userId") String userCookie, HttpServletResponse response) {
        Optional<Integer> currentUserId = checkLogin(userCookie);
        return currentUserId.map(this::startGame)
            .orElseGet(() -> logoutAndRedirect(response));
    }

    private String startGame(int currentUserId) {
        int gameId = chessService.getAddedGameId(
            new Game(currentUserId, false, LocalDateTime.now(ZoneId.of("Asia/Seoul"))));
        return REDIRECT + "/games/" + gameId;
    }

    @PostMapping("/move/{id}")
    private String move(@PathVariable("id") int gameId, @RequestParam String command,
        HttpServletResponse response) throws SQLException {
        ChessGame chessGame = chessService.reloadAllHistory(gameId);
        try {
            chessGame.move(command);
            if (chessGame.isEnd()) {
                chessService.updateGameIsEnd(gameId);
            }
        } catch (IllegalArgumentException e) {
            Cookie cookie = new Cookie("em", encodeCookie(e.getMessage()));
            cookie.setPath( "/games/" + gameId);
            response.addCookie(cookie);
            return REDIRECT + "/games/" + gameId;
        }

        chessService.addGameHistory(
            new GameHistory(gameId, command, LocalDateTime.now(ZoneId.of("Asia/Seoul"))));
        return REDIRECT + "/games/" + gameId;
    }

    private String encodeCookie(String cookie) {
        if (cookie == null) {
            return null;
        }
        return new String(Base64.getUrlEncoder().withoutPadding()
            .encode(cookie.getBytes(StandardCharsets.UTF_8)));
    }

    private String decodeCookie(String cookie) {
        if (cookie == null) {
            return null;
        }
        return new String(Base64.getUrlDecoder().decode(cookie));
    }

    private Map<String, Object> getCurrentGameMap(int currentUserId, String error,
        int currentGameId,
        WebBoardDto webBoardDto) {
        Map<String, Object> model = webBoardDto.getWebBoard();
        model.put("user", currentUserId);
        if (!StringUtils.isEmpty(error)) {
            model.put("em", error);
        }
        model.put("gameId", currentGameId);
        model.put("turn", webBoardDto.getTurn());
        model.put("isEnd", webBoardDto.isEnd());
        model.put("winner", webBoardDto.getWinner());
        return model;
    }
}
