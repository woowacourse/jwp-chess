package wooteco.chess;

import wooteco.chess.controller.WebUIChessController;

import java.sql.SQLException;

public class SparkChessApplication {
    public static void main(String[] args) throws SQLException {
        WebUIChessController chessGame = new WebUIChessController();
        chessGame.run();
    }
}
