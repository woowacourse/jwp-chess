package wooteco.chess;

import wooteco.chess.controller.SparkChessController;
import wooteco.chess.service.ChessGameService;

import java.sql.SQLException;

public class SparkChessApplication {
    public static void main(String[] args) throws SQLException {
        SparkChessController chessGame = new SparkChessController(new ChessGameService());
        chessGame.run();
    }
}
