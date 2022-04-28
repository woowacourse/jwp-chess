package chess.application.web;

import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/index")
    public String index(Model model) {
        model.addAllAttributes(gameService.modelReady());
        return "index";
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

    @GetMapping("/start")
    public String start(Model model) {
        gameService.start();
        return play(model);
    }

    @GetMapping("/load")
    public String load(Model model) {
        gameService.load();
        return play(model);
    }

    @PostMapping("/move")
    public String move(Model model, @RequestParam String source, @RequestParam String target) {
        gameService.move(source, target);
        if (gameService.isGameFinished()) {
            return end(model);
        }
        return play(model);
    }

    private String play(Model model) {
        model.addAllAttributes(gameService.modelPlayingBoard());
        return "index";
    }

    @GetMapping("/end")
    public String end(Model model) {
        model.addAllAttributes(gameService.end());
        return "result";
    }
}
