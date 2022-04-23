package chess.application.web.spark;

import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

import chess.application.web.JsonTransformer;
import chess.application.web.GameService;
import java.util.Map;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class WebApplication {

    public static void main(String[] args) {
        staticFileLocation("/static");
        GameService gameService = new GameService();
        JsonTransformer jsonTransformer = new JsonTransformer();

        get("/", (req, res) -> render(gameService.modelReady(), "index.html"));

        get("/start", (req, res) -> {
            gameService.start();
            res.redirect("/play");
            return null;
        });

        get("/load", (req, res) -> {
            gameService.load();
            res.redirect("/play");
            return null;
        });

        get("/play", (req, res) -> render(gameService.modelPlayingBoard(), "index.html"));

        post("/move", (req, res) -> {
            gameService.move(req);
            if (gameService.isGameFinished()) {
                res.redirect("/end");
                return null;
            }
            res.redirect("/play");
            return null;
        });

        get("/status", (req, res) -> jsonTransformer.render(gameService.modelStatus()));

        get("/save", (req, res) -> {
            try {
                gameService.save();
            } catch (Exception e) {
                res.status(500);
                return res;
            }
            res.status(201);
            return res;
        });

        get("/end", (req, res) -> render(gameService.end(), "result.html"));

        exception(Exception.class, (exception, request, response) -> {
            response.status(400);
            response.body("[ERROR] " + exception.getMessage());
        });
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}
