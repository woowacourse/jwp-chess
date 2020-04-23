package wooteco.chess;

import static spark.Spark.*;

import wooteco.chess.controller.SparkController;
import wooteco.chess.service.ChessService;

public class SparkChessApplication {
	public static void main(String[] args) {
		port(4567);
		staticFileLocation("/templates");

		ChessService chessService = new ChessService();
		SparkController sparkController = new SparkController(chessService);
		sparkController.run();
	}
}
