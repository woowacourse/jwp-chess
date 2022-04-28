package chess.application.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/list")
    public ResponseEntity<String> list() {
        String listData = jsonTransformer.render(gameService.list());
        return ResponseEntity.ok().body(listData);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestParam String title, @RequestParam String password) {
        gameService.createRoom(title, password);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/check-password/{gameNo}")
    public ResponseEntity<Void> checkPassword(@PathVariable int gameNo, @RequestBody String password) {
        if ("test".equals(password)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/load/{gameNo}")
    public String load(Model model, @PathVariable int gameNo, @RequestParam String password) {
        if ("test".equals(password)) {
            gameService.load(gameNo);
            return play(model, gameNo);
        }
        throw new IllegalArgumentException("비밀번호를 확인하세요.");
    }

    @PostMapping("/move/{gameNo}")
    public String move(Model model, @PathVariable int gameNo, @RequestParam String source,
                       @RequestParam String target) {
        gameService.move(source, target);
        if (gameService.isGameFinished()) {
            return end(model);
        }
        return play(model, gameNo);
    }

    private String play(Model model, long gameNo) {
        model.addAttribute("gameNo", gameNo);
        model.addAttribute("title", gameService.loadGameTitle(gameNo));
        model.addAllAttributes(gameService.modelPlayingBoard());
        return "game";
    }

    @GetMapping("/status")
    public ResponseEntity<String> status() {
        String statusData = jsonTransformer.render(gameService.modelStatus());
        return ResponseEntity.ok().body(statusData);
    }

    @GetMapping("/save/{gameNo}")
    public ResponseEntity<Void> save(@PathVariable int gameNo) {
        gameService.save(gameNo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/end")
    public String end(Model model) {
        model.addAllAttributes(gameService.end());
        return "result";
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("[ERROR] " + exception.getMessage());
    }
}
