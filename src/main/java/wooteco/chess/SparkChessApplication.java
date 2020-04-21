package wooteco.chess;

import static spark.Spark.*;

import wooteco.chess.controller.SparkChessController;
import wooteco.chess.repository.GameDAO;
import wooteco.chess.repository.JDBCGameDAO;
import wooteco.chess.service.GameService;
import wooteco.chess.utils.jdbc.DataSource;
import wooteco.chess.utils.jdbc.JDBCTemplate;
import wooteco.chess.utils.jdbc.MySQLDataSource;

public class SparkChessApplication {
	public static void main(String[] args) {
		port(8080);
		staticFiles.location("/templates");
		DataSource dataSource = new MySQLDataSource();
		GameDAO gameDAO = new JDBCGameDAO(new JDBCTemplate(dataSource));
		GameService gameService = new GameService(gameDAO);
		SparkChessController controller = new SparkChessController(gameService);
		controller.run();
	}
}
