package wooteco.chess;

import static spark.Spark.*;

import wooteco.chess.controller.SparkChessController;
import wooteco.chess.repository.DataSource;
import wooteco.chess.repository.GameRepository;
import wooteco.chess.repository.MySQLDataSource;
import wooteco.chess.repository.jdbc.JDBCGameRepository;
import wooteco.chess.repository.jdbc.JDBCTemplate;
import wooteco.chess.service.GameService;

public class SparkChessApplication {
	public static void main(String[] args) {
		port(8080);
		staticFiles.location("/templates");
		DataSource dataSource = new MySQLDataSource();
		GameRepository gameRepository = new JDBCGameRepository(new JDBCTemplate(dataSource));
		GameService gameService = new GameService(gameRepository);
		SparkChessController controller = new SparkChessController(gameService);
		controller.run();
	}
}
