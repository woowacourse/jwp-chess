package chess.controller;

import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    private final ChessService chessService;

    public IndexController(final ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("rooms", chessService.loadGameLists());
        return "index";
    }

    @GetMapping("/newgame")
    public String newGame() {
        return "new";
    }

    @GetMapping("/create")
    public String createGame(@RequestParam String gameID, @RequestParam String gamePW, Model model) {
        chessService.createGame(gameID, gamePW);
        model.addAttribute("rooms", chessService.loadGameLists());
        return "redirect:/";
    }

    @GetMapping("/checkPW")
    public String checkPassword(@RequestParam String gameCode, Model model) {
        final String gameID = chessService.findGameID(gameCode);
        model.addAttribute("gameID", gameID);
        return "delete";
    }

    @DeleteMapping("/delete/{gameID}/{inputPW}")
    public String deleteGame(@PathVariable String gameID, @PathVariable String inputPW, Model model) {
        chessService.checkAndDeleteGame(gameID, inputPW);
        model.addAttribute("rooms", chessService.loadGameLists());
        return "redirect:/";
    }
}
