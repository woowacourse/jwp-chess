package chess;

import chess.controller.SparkChessController;

public class SparkChessApplication {
    public static void main(String[] args) {
        final SparkChessController sparkChessController = new SparkChessController();
        sparkChessController.run();
    }
}
