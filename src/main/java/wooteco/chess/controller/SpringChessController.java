package wooteco.chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wooteco.chess.service.SpringChessService;

import java.sql.SQLException;
import java.util.Map;

@Controller
public class SpringChessController {
    private final SpringChessService service;

    public SpringChessController(SpringChessService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String chessGame() {
        return "index";
    }

    @PostMapping("/ready")
    public String enterGameRoom(@RequestParam(value = "game_id") String gameId, Model model) throws SQLException {
        boolean canResume = service.canResume(gameId);

        model.addAttribute("canResume", canResume);
        model.addAttribute("game_id", gameId);
        return "game_room";
    }

    @PostMapping("/play")
    public String startGame(@RequestParam(value = "game_id") String gameId, Model model) throws SQLException {
        service.startNewGame(gameId);

        Map<String, Object> gameInfo = service.provideGameInfo(gameId);
        model.addAllAttributes(gameInfo);
        model.addAttribute("game_id", gameId);
        return "game_room";
    }

    @PostMapping("/resume")
    public String resumeGame(@RequestParam(value = "game_id") String gameId, Model model) throws SQLException {
        service.resumeGame(gameId);

        Map<String, Object> gameInfo = service.provideGameInfo(gameId);
        model.addAllAttributes(gameInfo);
        model.addAttribute("game_id", gameId);
        return "game_room";
    }

    @PostMapping("/move")
    public String move(@RequestParam(value = "game_id") String gameId, @RequestParam String source,
                       @RequestParam String target, Model model) throws SQLException {
        service.move(gameId, source, target);

        Map<String, Object> gameInfo = service.provideGameInfo(gameId);
        model.addAllAttributes(gameInfo);
        model.addAttribute("game_id", gameId);
        model.addAttribute("end", service.provideWinner(gameId));
        return "game_room";
    }
}
