package chess;

import chess.contoller.ChessSparkController;
import chess.service.ChessService;

public class SparkChessApplication {

    public static void main(String[] args) {
        ChessSparkController chessSparkController = new ChessSparkController(new ChessService());
        chessSparkController.run();
    }
}
