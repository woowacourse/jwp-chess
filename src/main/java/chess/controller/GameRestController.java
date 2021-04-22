package chess.controller;

import chess.controller.dto.ReachablePositionsDto;
import chess.domain.board.position.Position;
import chess.service.GameService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@RequestMapping("/game")
public class GameRestController {
    private final GameService gameService;

    public GameRestController(final GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/reachable/{roomId}")
    public ReachablePositionsDto reachable(@PathVariable final Long roomId, @RequestParam final Position source) {
        final List<String> reachable = gameService.reachable(roomId, source);
        return new ReachablePositionsDto(reachable);
    }

    @PostMapping("/move/{roomId}")
    public RedirectView move(@PathVariable final Long roomId,
                       @RequestParam final Position source, @RequestParam final Position target) {
        gameService.move(roomId, source, target);
        if (gameService.isGameEnd(roomId)) {
            return new RedirectView("/game/result/" + roomId);
        }
        return new RedirectView( "/game/load/" + roomId);
    }
}