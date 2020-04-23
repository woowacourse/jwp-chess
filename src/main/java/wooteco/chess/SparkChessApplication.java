package wooteco.chess;

import wooteco.chess.controller.ChessWebController;

import static spark.Spark.staticFileLocation;

public class SparkChessApplication {
    public static void main(String[] args) {
        staticFileLocation("templates");
        ChessWebController chessWebController = new ChessWebController();
        chessWebController.run();
    }
}
