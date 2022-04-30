package chess.application.web;

import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {
    private final GameService gameService;

    public ViewController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/")
    public String games(Model model) {
        model.addAttribute("games", gameService.loadGames());
        return "games";
    }

    @GetMapping("/create")
    public String create() {
        return "create";
    }

    @PostMapping("/create")
    public String createGame(@RequestParam Map<String, String> request) {
        String title = request.get("title");
        String password = request.get("password");
        gameService.create(title, password);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, Model model) {
        model.addAttribute("id", id);
        return "delete";
    }

    @PostMapping("/delete/{id}")
    public String deleteGame(@PathVariable int id, @RequestParam Map<String, String> request) {
        gameService.deleteGame(id, request);
        return "redirect:/";
    }

    @GetMapping("/game/{id}")
    public String findGameById(@PathVariable int id, Model model) {
        Map<String, Object> result = gameService.findBoardByGameId(id);
        model.addAllAttributes(result);
        return "game";
    }

    @PostMapping("/game/{id}/move")
    public String move(Model model, @RequestParam Map<String, String> request, @PathVariable int id) {
        gameService.move(request.get("source"), request.get("target"));
        gameService.updateGame(id);
        if (gameService.isGameFinished()) {
            return end(model);
        }
        model.addAllAttributes(gameService.modelPlayingBoard());
        return "redirect:/game/" + id;
    }

    private String play(Model model) {
        model.addAllAttributes(gameService.modelPlayingBoard());
        return "index";
    }

    @GetMapping("/game/end")
    public String end(Model model) {
        model.addAllAttributes(gameService.end());
        return "result";
    }
}
