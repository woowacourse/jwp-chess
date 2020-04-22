package wooteco.chess;

import wooteco.chess.controller.SparkChessController;

import static spark.Spark.staticFiles;

public class SparkChessApplication {
    public static void main(String[] args) {
        staticFiles.location("/templates/public");

        SparkChessController sparkChessController = new SparkChessController();

        sparkChessController.route();
    }
}
