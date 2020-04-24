package wooteco.chess;

import com.google.gson.Gson;
import wooteco.chess.controller.SparkChessController;
import wooteco.chess.dao.DataSource;
import wooteco.chess.dao.JdbcChessGameDao;
import wooteco.chess.dao.JdbcConfiguration;
import wooteco.chess.dao.JdbcTemplate;
import wooteco.chess.service.ChessGameService;

public class SparkChessApplication {
	public static void main(String[] args) {
		new SparkChessController(new Gson(), createService()).run();
	}

	private static ChessGameService createService() {
		DataSource mySqlDataSource = generateDataSource();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(mySqlDataSource);
		JdbcChessGameDao jdbcChessGameDao = new JdbcChessGameDao(jdbcTemplate);
		return new ChessGameService(jdbcChessGameDao);
	}

	private static DataSource generateDataSource() {
		return new DataSource(new JdbcConfiguration.Builder()
				.url("jdbc:mysql://localhost:13306/chess_game")
				.option("?useSSL=false&serverTimezone=UTC")
				.username("root")
				.password("root")
				.driverClassName("com.mysql.cj.jdbc.Driver")
				.build());
	}
}
