package wooteco.chess;

import static spark.Spark.*;

import wooteco.chess.controller.SparkChessController;
import wooteco.chess.repository.DataSource;
import wooteco.chess.repository.GameDAO;
import wooteco.chess.repository.MySQLDataSource;
import wooteco.chess.repository.jdbc.JDBCGameDAO;
import wooteco.chess.repository.jdbc.JDBCTemplate;
import wooteco.chess.service.GameService;

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
