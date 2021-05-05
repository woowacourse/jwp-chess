package chess.controller.spring;

import chess.domain.ChessGame;
import chess.domain.dto.GameDto;
import chess.domain.dto.WebBoardDto;
import chess.domain.web.Game;
import chess.domain.web.GameHistory;
import chess.exception.ChessException;
import chess.service.ChessService;
import chess.service.UserService;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
public class ChessController {
    public static final String REDIRECT = "redirect:";
    private final ChessService chessService;
    private final UserService userService;

    public ChessController(ChessService chessService, UserService userService) {
        this.chessService = chessService;
        this.userService = userService;
    }

    @GetMapping("/games")
    private String renderGames(Model model, @CookieValue("userId") String cookie) {
        Optional<Integer> currentUserId = userService.checkLogin(cookie);
        return currentUserId.map(userId -> games(model, userId))
            .orElse(REDIRECT + "/logout");
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
        Optional<Integer> currentUserId = userService.checkLogin(userCookie);
        return currentUserId
            .map(id -> eachGame(model, gameId,
                getErrorMessage(response, gameId, errorMessageCookie)))
            .orElse(REDIRECT + "/logout");
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

    @PostMapping("/start")
    private String start(@CookieValue("userId") String userCookie) {
        Optional<Integer> currentUserId = userService.checkLogin(userCookie);
        return currentUserId.map(this::startGame)
            .orElse(REDIRECT + "/logout");
    }

    private String startGame(int currentUserId) {
        int gameId = chessService.getAddedGameId(
            new Game(currentUserId, false, LocalDateTime.now(ZoneId.of("Asia/Seoul"))));
        return REDIRECT + "/games/" + gameId;
    }

    @PostMapping("/move/{id}")
    private String move(@PathVariable("id") int gameId, @RequestParam String command) {
        ChessGame chessGame = chessService.reloadAllHistory(gameId);
        try {
            chessGame.move(command);
            if (chessGame.isEnd()) {
                chessService.updateGameIsEnd(gameId);
            }
        } catch (IllegalArgumentException e) {
            throw new ChessException(e.getMessage(), gameId);
        }

        chessService.addGameHistory(
            new GameHistory(gameId, command, LocalDateTime.now(ZoneId.of("Asia/Seoul"))));
        return REDIRECT + "/games/" + gameId;
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
