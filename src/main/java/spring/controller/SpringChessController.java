package spring.controller;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import spring.chess.game.ChessGame;
import spring.dto.LocationDto;
import spring.service.ChessService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

// TODO : Controller 분리 기준 알아보자.
@RestController
public class SpringChessController {
    private static final HandlebarsTemplateEngine handlebarsTemplateEngine = new HandlebarsTemplateEngine();

    private final ChessService chessService;

    // TODO : 생성자 주입이 더 나은 이유
    public SpringChessController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/main")
    public String main() {
        Map<String, Object> model = new HashMap<>();
        return render(model, "start.html");
    }

    @GetMapping("/start")
    public String start() {
        Map<String, Object> model = new HashMap<>();
        return render(model, "index.html");
    }

    @GetMapping("/start/boards")
    public String findBoards() throws SQLException {
        return chessService.findAllBoards();
    }

//    @GetMapping("/start/board")
//    public String findBoard(@RequestParam(name = "id") String id) throws SQLException {
//        int boardId = Integer.parseInt(id);
//        ChessGame chessGame = chessService.makeGameByDB(boardId);
//        return chessService.findGame(chessGame);
//    }

    @PostMapping("/start/move")
    public String move(@RequestParam(name = "now") String now, @RequestParam(name = "des") String des, @RequestParam(name = "game_id") String gameIdData) throws SQLException {
        LocationDto nowDto = new LocationDto(now);
        LocationDto destinationDto = new LocationDto(des);
        int gameId = Integer.parseInt(gameIdData);

        return chessService.move(nowDto, destinationDto, gameId);
    }

    @GetMapping("/start/winner")
    public String findWinner(@RequestParam(name = "game_id") String gameIdData) throws SQLException {
        int gameId = Integer.parseInt(gameIdData);
        ChessGame chessGame = chessService.makeGameByDB(gameId);
        return chessService.findWinner(chessGame);
    }

    @PostMapping("/end")
    public String end() {
        Map<String, Object> model = new HashMap<>();
        return render(model, "start.html");
    }

    @PostMapping("/start/new/game")
    public String startNewGame(@RequestParam(name = "game_id") String gameIdData) throws SQLException {
        int gameId = Integer.parseInt(gameIdData);
        ChessGame chessGame = new ChessGame();
        chessService.resetChessGame(chessGame, gameId);
        return chessService.findBoard(gameId);
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return handlebarsTemplateEngine.render(new ModelAndView(model, templatePath));
    }

    ///

    @PostMapping("/start/abc")
    public String starts() {
        return new Gson().toJson(chessService.makeChessBoard());
    }
}
