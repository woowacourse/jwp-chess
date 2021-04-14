package chess;

import static spark.Spark.before;
import static spark.Spark.port;
import static spark.Spark.staticFileLocation;

import chess.controller.SparkChessController;

public class SparkChessApplication {

    public static void main(String[] args) {
        port(8080);
        staticFileLocation("/public");
        before(((request, response) -> {
            response.type("application/json");
        }));

        SparkChessController webController = new SparkChessController();
        webController.run();
    }
}
