package chess;

import chess.controller.SparkChessController;
import spark.Spark;

public class SparkChessApplication {
    public static void main(String[] args) {
        Spark.staticFileLocation("/static");
        new SparkChessController().run();
    }
}
