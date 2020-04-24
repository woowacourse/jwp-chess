package wooteco.chess;

import wooteco.chess.controller.SparkController;
import wooteco.chess.service.ChessService;

import static spark.Spark.staticFiles;

public class SparkChessApplication {
    public static void main(String[] args) {
        staticFiles.location("/public");

        SparkController controller = new SparkController(new ChessService());
        controller.play();
    }
}
