package chess.application.web.spring;

import chess.application.web.JsonTransformer;
import chess.application.web.WebGameController;
import java.sql.SQLException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SpringBootApplication
@Controller
public class SpringChessApplication {
    private final WebGameController webGameController;
    private final JsonTransformer jsonTransformer;

    public SpringChessApplication() {
        this.webGameController = new WebGameController();
        this.jsonTransformer = new JsonTransformer();
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringChessApplication.class, args);
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAllAttributes(webGameController.modelReady());
        return "index";
    }

    @GetMapping("/start")
    public String start(Model model) {
        webGameController.start();
        return play(model);
    }

    @GetMapping("/load")
    public String load(Model model) throws SQLException {
        webGameController.load();
        return play(model);
    }

    @PostMapping("/move")
    public String move(Model model, @RequestParam String source, @RequestParam String target) {
        webGameController.move(source, target);
        if (webGameController.isGameFinished()) {
            return end(model);
        }
        return play(model);
    }

    private String play(Model model) {
        model.addAllAttributes(webGameController.modelPlayingBoard());
        return "index";
    }

    @GetMapping("/status")
    public ResponseEntity<String> status() {
        String statusData = jsonTransformer.render(webGameController.modelStatus());
        return ResponseEntity.ok().body(statusData);
    }

    @GetMapping("/save")
    public ResponseEntity<Void> save() {
        try {
            webGameController.save();
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/end")
    public String end(Model model) {
        model.addAllAttributes(webGameController.end());
        return "result";
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("[ERROR] " + exception.getMessage());
    }
}
