package chess.application.web;

import chess.domain.ChessGame;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<Void> create(@RequestParam String title, @RequestParam String password) {
        gameService.createRoom(title, password);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/game/{gameNo}")
    public ResponseEntity<String> delete(@PathVariable long gameNo, @RequestBody String password) {
        try {
            gameService.checkPassword(gameNo, password);
            gameService.delete(gameNo);
            return createMessageResponse(HttpStatus.OK, "방이 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            return createMessageResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (IllegalStateException e) {
            return createMessageResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    private ResponseEntity<String> createMessageResponse(HttpStatus status, String message) {
        String messageData = jsonTransformer.render(Map.of("message", message));
        return ResponseEntity.status(status).body(messageData);
    }

    @PostMapping("/game/{gameNo}")
    public String load(Model model, @PathVariable int gameNo, @RequestParam String password) {
        gameService.checkPassword(gameNo, password);
        gameService.load(gameNo);
        return play(model, gameNo);
    }

    @PutMapping("/board/{gameNo}")
    public String move(Model model, @PathVariable int gameNo,
                       @RequestParam String source, @RequestParam String target) {
        ChessGame chessGame = gameService.load(gameNo);
        gameService.move(source, target, chessGame);
        gameService.save(gameNo, chessGame);
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

    @ExceptionHandler
    public ResponseEntity<String> handle(Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("[ERROR] " + exception.getMessage());
    }
}
