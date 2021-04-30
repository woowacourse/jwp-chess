package chess.controller;

import chess.domain.board.position.Position;
import chess.domain.piece.Owner;
import chess.dto.ReachablePositionsDto;
import chess.service.GameService;
import chess.service.PlayerService;
import chess.util.CookieHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RestController
@RequestMapping("/game")
public class GameRestController {
    private final GameService gameService;
    private final PlayerService playerService;
    private final CookieHandler cookieHandler;

    public GameRestController(final GameService gameService,
                              final PlayerService playerService,
                              final CookieHandler cookieHandler) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.cookieHandler = cookieHandler;
    }

    @GetMapping("/reachable/{roomId}")
    public ReachablePositionsDto reachable(@PathVariable final Long roomId,
                                           @RequestParam final Position source,
                                           final HttpServletRequest request,
                                           final HttpServletResponse response) {
        final Cookie cookie = cookieHandler.search(roomId, request);
        validatePlayerCookie(cookie);
        cookieHandler.extendAge(cookie, response);

        final Owner owner = playerService.ownerOfPlayer(roomId, cookie.getValue());
        return new ReachablePositionsDto(gameService.reachable(roomId, source, owner));
    }

    private void validatePlayerCookie(final Cookie cookie) {
        if (Objects.isNull(cookie)) {
            throw new IllegalArgumentException("사용자 비밀번호가 없습니다.");
        }
    }
}