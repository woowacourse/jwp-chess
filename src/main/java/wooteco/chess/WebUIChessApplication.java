package wooteco.chess;

import static spark.Spark.*;

import wooteco.chess.controller.WebChessController;
import wooteco.chess.dao.ChessGameDao;
import wooteco.chess.dao.ChessHistoryDao;
import wooteco.chess.dao.MySqlChessGameDao;
import wooteco.chess.dao.MySqlChessHistoryDao;
import wooteco.chess.database.ConnectionManager;
import wooteco.chess.database.JdbcTemplate;
import wooteco.chess.database.MySqlConnectionManager;
import wooteco.chess.service.ChessService;

public class WebUIChessApplication {

	public static void main(String[] args) {
		port(8080);
		staticFileLocation("/public");

		WebChessController webChessController = initWebController();
		webChessController.run();
	}

	private static WebChessController initWebController() {
		ConnectionManager connectionManager = MySqlConnectionManager.getInstance();
		JdbcTemplate template = new JdbcTemplate(connectionManager);

		ChessGameDao chessGameDao = new MySqlChessGameDao(template);
		ChessHistoryDao chessHistoryDao = new MySqlChessHistoryDao(template);
		ChessService chessService = new ChessService(chessGameDao, chessHistoryDao);

		return new WebChessController(chessService);
	}

}
