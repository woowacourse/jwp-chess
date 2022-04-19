package chess;

import chess.web.controller.ChessWebController;
import chess.web.service.ChessService;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class SparkChessApplication {
    public static void main(String[] args) {
        port(8080);
        staticFileLocation("static");
        ChessWebController chessController = new ChessWebController(new ChessService());
        chessController.run();
    }
}
