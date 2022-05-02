package chess.application.web;

import chess.domain.ChessGame;
import org.springframework.http.ResponseEntity;
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
    private final JsonTransformer jsonTransformer;

    public GameController(GameService gameService) {
        this.jsonTransformer = new JsonTransformer();
        this.gameService = gameService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/games")
    public ResponseEntity<String> list() {
        String listData = jsonTransformer.render(gameService.list());
        return ResponseEntity.ok().body(listData);
    }

    @PostMapping("/game")
    public String create(@RequestParam String title, @RequestParam String password) {
        gameService.createRoom(title, password);
        return index();
    }

    @DeleteMapping("/game/{gameNo}")
    public String delete(@PathVariable long gameNo, @RequestParam String password) {
        gameService.delete(gameNo, password);
        return index();
    }

    @GetMapping("/game/{gameNo}")
    public String load(Model model, @PathVariable int gameNo) {
        gameService.load(gameNo);
        return play(model, gameNo);
    }

    @PutMapping("/board/{gameNo}")
    public String move(Model model, @PathVariable int gameNo,
                       @RequestParam String source, @RequestParam String target) {
        ChessGame chessGame = gameService.load(gameNo);
        gameService.move(gameNo, chessGame, source, target);
        return play(model, gameNo);
    }

    private String play(Model model, long gameNo) {
        ChessGame chessGame = gameService.load(gameNo);
        if (gameService.isGameFinished(gameNo, chessGame)) {
            return end(model, gameNo);
        }
        model.addAttribute("gameNo", gameNo);
        model.addAttribute("title", gameService.loadGameTitle(gameNo));
        model.addAllAttributes(gameService.modelPlayingBoard(chessGame));
        return "game";
    }

    @GetMapping("/score/{gameNo}")
    public ResponseEntity<String> status(@PathVariable int gameNo) {
        ChessGame chessGame = gameService.load(gameNo);
        String statusData = jsonTransformer.render(gameService.modelStatus(chessGame));
        return ResponseEntity.ok().body(statusData);
    }

    @PutMapping("/game/{gameNo}")
    public String end(Model model, @PathVariable long gameNo) {
        ChessGame chessGame = gameService.load(gameNo);
        model.addAllAttributes(gameService.end(gameNo, chessGame));
        return "result";
    }
}
