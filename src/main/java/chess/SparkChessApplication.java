package chess;

import static spark.Spark.*;

import chess.controller.WebController;

public class SparkChessApplication {
    public static void main(String[] args) {
        staticFileLocation("/static");

        WebController webController = new WebController();
        webController.run();
    }
}
