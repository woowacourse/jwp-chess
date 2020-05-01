package spring.controller;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import spring.dto.LocationDto;
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
        model.put("games", chessService.findAllGame());
        return render(model, "start.html");
    }

    @GetMapping("/index/{game_id}")
    public String start(@PathVariable(name = "game_id") Long gameId) {
        Map<String, Object> model = new HashMap<>();
        model.put("game_id", gameId);
        return render(model, "index.html");
    }

    @GetMapping("/api/game/{game_id}")
    public String load(@PathVariable(name = "game_id") Long gameId) throws SQLException {
        return GSON.toJson(chessService.resumeGame(gameId));
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
