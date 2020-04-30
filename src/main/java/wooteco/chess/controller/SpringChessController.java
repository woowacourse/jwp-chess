package wooteco.chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wooteco.chess.service.SpringChessService;

@Controller
public class SpringChessController {
    private final SpringChessService springChessService;

    public SpringChessController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    @GetMapping("/")
    public String chessGame() {
        return "index";
    }

    @PostMapping("/ready")
    public String enterGameRoom(@RequestParam(value = "game_id") String gameId, Model model) {
        boolean canResume = springChessService.canResume(gameId);

        model.addAttribute("canResume", canResume);
        model.addAttribute("game_id", gameId);
        return "game_room";
    }

    @PostMapping("/play")
    public String startGame(@RequestParam(value = "game_id") String gameId, Model model) {
        springChessService.startNewGame(gameId);

        model.addAllAttributes(springChessService.provideGameInfo(gameId));
        model.addAttribute("game_id", gameId);
        return "game_room";
    }

    @PostMapping("/resume")
    public String resumeGame(@RequestParam(value = "game_id") String gameId, Model model) {
        springChessService.resumeGame(gameId);

        model.addAllAttributes(springChessService.provideGameInfo(gameId));
        model.addAttribute("game_id", gameId);
        return "game_room";
    }

    @PostMapping("/move")
    public String move(@RequestParam(value = "game_id") String gameId, @RequestParam String source,
                       @RequestParam String target, Model model) {
        springChessService.move(gameId, source, target);

        model.addAllAttributes(springChessService.provideGameInfo(gameId));
        model.addAttribute("game_id", gameId);
        model.addAttribute("end", springChessService.provideWinner(gameId));
        return "game_room";
    }
}
