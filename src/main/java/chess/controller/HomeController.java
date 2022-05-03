package chess.controller;

import chess.service.ChessService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private final ChessService chessService;

    public HomeController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String home(Model model, @RequestParam(value = "error", required = false) String error) {
        List<String> gameNames = chessService.findAllGameName();
        model.addAttribute("gameNames", gameNames);
        model.addAttribute("error", error);
        return "index";
    }

    @GetMapping("/load")
    public String restart(@RequestParam("game_name") String gameName) {
        Long id = chessService.findIdByGameName(gameName);

        return "redirect:/game/" + id;
    }

    @PostMapping("/start")
    public String start(@RequestParam("game_name") String gameName, @RequestParam("password") String password) {
        Long id = chessService.save(gameName, password);

        return "redirect:/game/" + id;
    }

    @PostMapping("/delete")
    public String delete(@RequestParam(value = "game_name", required = false) String gameName,
                         @RequestParam(value = "password", required = false) String password) {
        chessService.deleteBy(gameName, password);

        return "redirect:/";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
