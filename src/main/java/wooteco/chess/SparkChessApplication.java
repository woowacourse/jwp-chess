package wooteco.chess;

import static spark.Spark.*;

import wooteco.chess.controller.SparkChessController;
import wooteco.chess.dao.ChessGameDao;
import wooteco.chess.dao.ChessHistoryDao;
import wooteco.chess.dao.MySqlChessGameDao;
import wooteco.chess.dao.MySqlChessHistoryDao;
import wooteco.chess.database.DataSource;
import wooteco.chess.database.JdbcTemplate;
import wooteco.chess.database.MySqlDataSource;
import wooteco.chess.service.ChessService;

public class SparkChessApplication {

	public static void main(String[] args) {
		port(8080);
		staticFileLocation("/public");

		SparkChessController sparkChessController = initWebController();
		sparkChessController.run();
	}

	private static SparkChessController initWebController() {
		DataSource dataSource = MySqlDataSource.getInstance();
		JdbcTemplate template = new JdbcTemplate(dataSource);

		ChessGameDao chessGameDao = new MySqlChessGameDao(template);
		ChessHistoryDao chessHistoryDao = new MySqlChessHistoryDao(template);
		ChessService chessService = new ChessService(chessGameDao, chessHistoryDao);

		return new SparkChessController(chessService);
	}

}
