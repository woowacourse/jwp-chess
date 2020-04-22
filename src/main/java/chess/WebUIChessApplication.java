package chess;

import chess.controller.WebChessController;

import java.sql.SQLException;

public class WebUIChessApplication {
    public static void main(String[] args) {
        WebChessController webChessController = null;
        try {
            webChessController = new WebChessController();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        webChessController.run();
    }
}
