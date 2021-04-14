package chess;

import chess.controller.WebController;
import chess.domain.ChessGameManager;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.Map;

import static spark.Spark.staticFileLocation;

public class SparkChessApplication {
    public static void main(String[] args) {
        staticFileLocation("public");

        WebController webController = new WebController(new ChessGameManager());
        webController.run();
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}
