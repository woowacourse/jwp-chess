package chess.controller;

import chess.service.ChessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String newGame(){
        return "newgame";
    }

    @GetMapping("/create")
    public String createGame(@RequestParam String gameID, @RequestParam String gamePW, Model model){
        chessService.createGame(gameID, gamePW);
        model.addAttribute("rooms", chessService.loadGameLists());
        return "index";
    }

    @GetMapping("/delete")
    public String deleteGame(@RequestParam String gameID, Model model){
        chessService.deleteGame(gameID);
        model.addAttribute("rooms", chessService.loadGameLists());
        return "index";
    }
}
