package wooteco.chess;

import static spark.Spark.*;

import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;
import wooteco.chess.domain.player.User;
import wooteco.chess.service.ChessService;
import wooteco.chess.util.JsonTransformer;

public class SparkChessApplication {

    public static void main(String[] args) {

        Spark.staticFiles.location("/templates");

        ChessService chessService = new ChessService();

        get("/main", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("rows", chessService.getEmptyRowsDto());
            return render(model, "main.html");
        });

        post("/start", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String blackUserName = req.queryParams("blackUserName");
            String whiteUserName = req.queryParams("whiteUserName");
            User blackUser = new User(blackUserName);
            User whiteUser = new User(whiteUserName);

            model.put("blackUser", blackUserName);
            model.put("whiteUser", whiteUserName);
            model.put("rows", chessService.getRowsDto(blackUser, whiteUser));
            model.put("turn", chessService.getTurn(blackUser));
            return render(model, "board.html");
        });

        post("/path", (req, res) -> {
            String source = req.queryParams("source");
            String blackUserName = req.queryParams("blackUserName");
            try {
                return chessService.searchPath(new User(blackUserName), source);
            } catch (RuntimeException e) {
                return e.getMessage();
            }
        }, new JsonTransformer());

        post("/move", (req, res) -> {
            String source = req.queryParams("source");
            String target = req.queryParams("target");
            String blackUserName = req.queryParams("blackUserName");
            User blackUser = new User(blackUserName);
            Map<String, Object> model = new HashMap<>();
            model.put("status", false);
            model.put("message", "");
            try {
                chessService.move(blackUser, source, target);
                model.put("white", chessService.calculateWhiteScore(blackUser));
                model.put("black", chessService.calculateBlackScore(blackUser));
            } catch (RuntimeException e) {
                model.put("message", e.getMessage());
            }
            if (chessService.checkGameNotFinished(blackUser)) {
                model.put("status", true);
            }
            return model;
        }, new JsonTransformer());

        post("/save", (req, res) -> {
            String blackUserName = req.queryParams("blackUserName");
            String whiteUserName = req.queryParams("whiteUserName");
            chessService.save(new User(blackUserName), new User(whiteUserName));
            Map<String, Object> model = new HashMap<>();
            model.put("rows", chessService.getEmptyRowsDto());
            return render(model, "main.html");
        });

        post("/end", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String blackUserName = req.queryParams("blackUserName");
            String whiteUserName = req.queryParams("whiteUserName");
            chessService.delete(new User(blackUserName), new User(whiteUserName));
            model.put("rows", chessService.getEmptyRowsDto());
            return render(model, "main.html");
        });
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}
