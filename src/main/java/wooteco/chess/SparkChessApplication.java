package wooteco.chess;

import wooteco.chess.contoller.WebController;
import wooteco.chess.domain.dao.RoomDao;
import wooteco.chess.domain.service.ChessGameService;

public class SparkChessApplication {
	public static void main(String[] args) {
		ChessGameService chessGameService = new ChessGameService(new RoomDao());
		WebController webController = new WebController(chessGameService);
		webController.run();
	}
}
