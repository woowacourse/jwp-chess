package wooteco.chess;

import wooteco.chess.controller.WebController;
import wooteco.chess.dao.RoomDao;
import wooteco.chess.service.ChessGameService;

public class SparkChessApplication {
	public static void main(String[] args) {
		ChessGameService chessGameService = new ChessGameService(new RoomDao());
		WebController webController = new WebController(chessGameService);
		webController.run();
	}
}
