package chess.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import chess.dto.ChessGameDto;
import chess.service.GamesService;

@Controller
@RequestMapping("/")
public class MainController {

    private final GamesService gamesService;

    public MainController(GamesService gamesService) {
        this.gamesService = gamesService;
    }

    @GetMapping
    public String index() {
        return "index";
    }

    @PostMapping("/write")
    public String write() {
        return "write";
    }

    @PostMapping("/create")
    public String create(@RequestParam Map<String, String> map) {
        String title = map.get("title");
        String password = map.get("password");
        gamesService.create(new ChessGameDto(title, password));
        return "redirect:/";
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("list", gamesService.loadGames());
        return "list";
    }

    @GetMapping("/game/{id}")
    public String board(@PathVariable Long id) {
        return "board";
    }
}
