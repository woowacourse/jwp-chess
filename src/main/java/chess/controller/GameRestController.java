package chess.controller;

import chess.domain.board.position.Position;
import chess.domain.piece.Owner;
import chess.dto.ReachablePositionsDto;
import chess.service.GameService;
import chess.service.PlayerService;
import chess.util.CookieHandler;
import com.sun.jndi.toolkit.url.Uri;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
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
    public ResponseEntity reachable(@PathVariable final Long roomId,
                                    @RequestParam final Position source,
                                    final HttpServletRequest request,
                                    final HttpServletResponse response) {
        final Cookie cookie = cookieHandler.search(roomId, request);
        cookieHandler.validatePlayerCookie(cookie);
        cookieHandler.extendAge(cookie, response);

        final Owner owner = playerService.ownerOfPlayer(roomId, cookie.getValue());
        final ReachablePositionsDto reachablePositions = new ReachablePositionsDto(gameService.reachable(roomId, source, owner));
        return ResponseEntity.ok(reachablePositions);
    }



    @PostMapping("/move/{roomId}")
    public ResponseEntity move(@PathVariable final Long roomId,
                               @RequestBody final Map<String, Position> positions) {
        gameService.move(roomId, positions.get("source"), positions.get("target"));
        return ResponseEntity.ok("/game/load/" + roomId);
    }
}