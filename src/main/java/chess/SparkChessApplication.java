package chess;

import chess.contoller.SparkChessController;
import chess.service.SparkChessService;

public class SparkChessApplication {

    public static void main(String[] args) {
        SparkChessController sparkChessController = new SparkChessController(new SparkChessService());
        sparkChessController.run();
    }
}
