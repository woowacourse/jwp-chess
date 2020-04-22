package chess;

import java.sql.SQLException;

import chess.controller.WebChessController;

public class WebUIChessApplication {
	public static void main(String[] args) {
		try {
			WebChessController webChessController = new WebChessController();
			webChessController.run();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
