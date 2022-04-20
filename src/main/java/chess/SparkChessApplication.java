package chess;

import static spark.Spark.*;

import java.util.Map;

import chess.config.ControllerConfig;
import chess.controller.SparkChessController;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class SparkChessApplication {
    public static void main(String[] args) {
        SparkChessController controller = ControllerConfig.getWebController();

        staticFileLocation("/static");
        port(8081);

        get("/", controller::index);
        get("/main", controller::main);
        get("/create", controller::create);
        get("/enter", controller::enter);
        get("/start", controller::start);
        get("/end", controller::end);
        post("/move", controller::move);
        get("status", controller::status);
        exception(RuntimeException.class, controller::handleException);
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}
