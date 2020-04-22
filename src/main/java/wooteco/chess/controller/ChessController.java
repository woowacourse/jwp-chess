package wooteco.chess.controller;

import spark.ModelAndView;
import spark.Request;
import spark.template.handlebars.HandlebarsTemplateEngine;
import wooteco.chess.db.ChessPieceDao;
import wooteco.chess.db.MoveHistoryDao;
import wooteco.chess.service.ChessWebService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ChessController {
    private final ChessWebService service;
    private final HandlebarsTemplateEngine templateEngine = new HandlebarsTemplateEngine();

    public ChessController() {
        this.service = new ChessWebService(new ChessPieceDao(), new MoveHistoryDao());
    }

    public String chessGame() {
        return render(templateEngine, new HashMap<>(), "index.html");
    }

    public String enterGameRoom(Request req) throws SQLException {
        String gameId = req.queryParams("game_id");

        boolean canResume = service.canResume(gameId);

        Map<String, Object> model = new HashMap<>();
        model.put("canResume", canResume);
        model.put("game_id", gameId);
        return render(templateEngine, model, "game_room.html");
    }

    public String startGame(Request req) throws SQLException {
        String gameId = req.queryParams("game_id");

        service.startNewGame(gameId);

        Map<String, Object> gameInfo = service.provideGameInfo(gameId);
        Map<String, Object> model = new HashMap<>(gameInfo);
        model.put("game_id", gameId);
        return render(templateEngine, model, "game_room.html");
    }

    public String resumeGame(Request req) throws SQLException {
        String gameId = req.queryParams("game_id");

        service.resumeGame(gameId);

        Map<String, Object> gameInfo = service.provideGameInfo(gameId);
        Map<String, Object> model = new HashMap<>(gameInfo);
        model.put("game_id", gameId);
        return render(templateEngine, model, "game_room.html");
    }

    public String move(Request req) throws SQLException {
        String gameId = req.queryParams("game_id");
        String source = req.queryParams("source");
        String target = req.queryParams("target");

        service.move(gameId, source, target);

        Map<String, Object> gameInfo = service.provideGameInfo(gameId);
        Map<String, Object> model = new HashMap<>(gameInfo);
        model.put("game_id", gameId);
        model.put("end", service.provideWinner(gameId));
        return render(templateEngine, model, "game_room.html");
    }

    private String render(HandlebarsTemplateEngine templateEngine, Map<String, Object> model, String templatePath) {
        return templateEngine.render(new ModelAndView(model, templatePath));
    }
}
