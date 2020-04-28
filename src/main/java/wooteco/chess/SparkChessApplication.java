package wooteco.chess;

import static spark.Spark.*;

import wooteco.chess.controller.SparkChessController;
import wooteco.chess.repository.DataSource;
import wooteco.chess.repository.GameDao;
import wooteco.chess.repository.MySQLDataSource;
import wooteco.chess.repository.jdbc.JDBCGameDao;
import wooteco.chess.repository.jdbc.JDBCTemplate;
import wooteco.chess.service.GameService;

public class SparkChessApplication {
	public static void main(String[] args) {
		port(8080);
		staticFiles.location("/templates");
		DataSource dataSource = new MySQLDataSource();
		GameDao gameDAO = new JDBCGameDao(new JDBCTemplate(dataSource));
		GameService gameService = new GameService(gameDAO);
		SparkChessController controller = new SparkChessController(gameService);
		controller.run();
	}
}
