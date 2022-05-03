package chess.application.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/game")
    public String create(@RequestParam String title, @RequestParam String password) {
        gameService.createRoom(title, password);
        return "redirect:/";
    }

    @DeleteMapping("/game/{gameNo}")
    public String delete(@PathVariable long gameNo, @RequestParam String password) {
        gameService.delete(gameNo, password);
        return "redirect:/";
    }

    @GetMapping("/game/{gameNo}")
    public String load(Model model, @PathVariable int gameNo) {
        return play(model, gameNo);
    }

    @PutMapping("/board/{gameNo}")
    public String move(Model model, @PathVariable int gameNo,
                       @RequestParam String source, @RequestParam String target) {
        gameService.move(gameNo, source, target);
        return play(model, gameNo);
    }

    private String play(Model model, long gameNo) {
        if (!gameService.isGameRunning(gameNo)) {
            return end(model, gameNo);
        }
        model.addAttribute("gameNo", gameNo);
        model.addAttribute("title", gameService.loadGameTitle(gameNo));
        model.addAllAttributes(gameService.modelPlayingBoard(gameNo));
        return "game";
    }

    @PutMapping("/game/{gameNo}")
    public String end(Model model, @PathVariable long gameNo) {
        model.addAllAttributes(gameService.end(gameNo));
        return "result";
    }
}
