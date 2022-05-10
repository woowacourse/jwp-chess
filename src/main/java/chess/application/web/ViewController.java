package chess.application.web;

import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ViewController {
    private final GameService gameService;

    public ViewController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/")
    public String games(Model model) {
        model.addAttribute("games", gameService.findAllGames());
        return "games";
    }

    @GetMapping("/create-view")
    public String createView() {
        return "create";
    }

    @GetMapping("/delete-view/{id}")
    public String delete(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        return "delete";
    }

    @GetMapping("/games/{id}")
    public String findGameById(@PathVariable Long id, Model model) {
        Map<String, Object> result = gameService.findBoardByGameId(id);
        model.addAllAttributes(result);
        return "game";
    }

    @GetMapping("/games/{id}/end")
    public String end(@PathVariable Long id, Model model) {
        model.addAllAttributes(gameService.end(id));
        return "result";
    }
}
