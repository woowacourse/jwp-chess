package chess;

import chess.controller.SparkChessController;
import spark.ModelAndView;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.Map;

import static spark.Spark.get;

public class SparkChessApplication {
    public static void main(String[] args) {
        Spark.staticFileLocation("/static");
        new SparkChessController().run();
    }
}
