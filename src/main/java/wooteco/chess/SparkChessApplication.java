package wooteco.chess;

import com.google.gson.Gson;
import wooteco.chess.controller.SparkChessController;
import wooteco.chess.dao.DataSource;
import wooteco.chess.dao.JdbcChessGameDao;
import wooteco.chess.dao.JdbcTemplate;
import wooteco.chess.dao.MySqlDataSource;
import wooteco.chess.service.ChessGameService;

public class SparkChessApplication {
	public static void main(String[] args) {
		new SparkChessController(new Gson(), createService()).run();
	}

	private static ChessGameService createService() {
		DataSource mySqlDataSource = new MySqlDataSource();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(mySqlDataSource);
		JdbcChessGameDao jdbcChessGameDao = new JdbcChessGameDao(jdbcTemplate);
		return new ChessGameService(jdbcChessGameDao);
	}
}
