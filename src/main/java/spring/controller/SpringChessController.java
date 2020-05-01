package spring.controller;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;
import spark.ModelAndView;
import spark.dto.LocationDto;
import spark.template.handlebars.HandlebarsTemplateEngine;
import spring.service.ChessService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SpringChessController {
    private static final HandlebarsTemplateEngine handlebarsTemplateEngine = new HandlebarsTemplateEngine();
    private static final Gson GSON = new Gson();

    private final ChessService chessService;

    public SpringChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String main() {
        Map<String, Object> model = new HashMap<>();
        return render(model, "start.html");
    }

    @GetMapping("/index")
    public String start() {
        Map<String, Object> model = new HashMap<>();
        return render(model, "index.html");
    }

    @GetMapping("/api/game")
    public String resume() throws SQLException {
        return GSON.toJson(chessService.resumeGame());
    }

    @PostMapping("/api/game")
    public String starts() {
        return new Gson().toJson(chessService.makeChessBoard());
    }

    @PutMapping("/api/piece")
    public String move(@RequestParam(name = "now") String now,
                       @RequestParam(name = "des") String destination,
                       @RequestParam(name = "game_id") String gameIdData) throws SQLException {
        LocationDto nowDto = new LocationDto(now);
        LocationDto destinationDto = new LocationDto(destination);
        long gameId = Long.parseLong(gameIdData);

        return GSON.toJson(chessService.move(gameId, nowDto, destinationDto));
    }

    @GetMapping("/api/games/{id}/result")
    public String winnerGame(@PathVariable Long id) throws SQLException {
        System.out.println("id는 " + id);
        chessService.findWinner(id);
        return GSON.toJson(chessService.findWinner(id));
    }

    @DeleteMapping("/api/games/{id}")
    public Long deleteGame(@PathVariable Long id) throws SQLException {
        chessService.endGame(id);
        return id;
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return handlebarsTemplateEngine.render(new ModelAndView(model, templatePath));
    }
}
