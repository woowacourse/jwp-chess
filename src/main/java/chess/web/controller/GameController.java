package chess.web.controller;

import chess.service.GameService;
import chess.web.dto.game.GameRequestDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/games")
@Controller
public class GameController {

    private final GameService gameService;

    public GameController(final GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public String showRooms() {
        return "game-list";
    }

    @PostMapping
    public String initializeChess(@RequestBody GameRequestDto gameRequestDto) {
        final long id = gameService.initializeGame(gameRequestDto);
        return "redirect:/games/" + id;
    }

    @GetMapping("/{id}")
    public String loadGameById() {
        return "chess";
    }

}
