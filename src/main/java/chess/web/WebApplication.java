package chess.web;

import static spark.Spark.port;
import static spark.Spark.staticFileLocation;

import chess.web.controller.ChessController;

public class WebApplication {
    public static void main(String[] args) {
        port(3456);
        staticFileLocation("/static");

        ChessController webChessController = new ChessController();
        webChessController.run();
    }
}
