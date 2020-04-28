package wooteco.chess;

import static spark.Spark.*;

import wooteco.chess.controller.SparkController;
import wooteco.chess.dao.BoardDao;
import wooteco.chess.dao.TurnDao;
import wooteco.chess.service.ChessService;

public class SparkChessApplication {
	public static void main(String[] args) {
		port(4567);
		staticFileLocation("/templates");

		ChessService chessService = new ChessService(new BoardDao(), new TurnDao());
		SparkController sparkController = new SparkController(chessService);
		sparkController.run();
	}
}
