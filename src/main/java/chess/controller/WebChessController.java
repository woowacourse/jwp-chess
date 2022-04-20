package chess.controller;

import static spark.Spark.get;

import chess.dao.DBConnectionSetup;
import chess.dao.GameDaoImpl;
import chess.dao.PieceDaoImpl;
import chess.service.ChessService;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class WebChessController {

    private static final int ILLEGAL_REQUEST_CODE = 400;
    private static final String GAME_ID_PARAM = "gameId";
    private static final String ILLEGAL_GAME_ID = "잘못된 게임 ID 입력입니다.";

    private final ChessService chessService = new ChessService(new GameDaoImpl(new DBConnectionSetup()),
            new PieceDaoImpl(new DBConnectionSetup()));

    public void run() {
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return render(model, "index.html");
        });

        get("/game", (req, res) -> {
            res.redirect("/game/" + req.queryParams(GAME_ID_PARAM));
            return null;
        });

        get("/game/:gameId", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return render(model, "game.html");
        });

    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }

    private Long parseGameId(String idString) {
        try {
            return Long.parseLong(idString);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ILLEGAL_GAME_ID);
        }
    }
}
