package chess;

import static spark.Spark.staticFiles;

import chess.service.ChessService;

public class SparkChessApplication {

    public static void main(String[] args) {
        staticFiles.location("/static");
        ChessController chessController = new ChessController(new ChessService());
        chessController.run();
    }
}
