package wooteco.chess;

import wooteco.chess.controller.spark.SparkController;
import wooteco.chess.db.ConnectionLoader;
import wooteco.chess.db.dao.BoardDao;
import wooteco.chess.db.dao.PlayerDao;
import wooteco.chess.db.dao.RoomDao;

public class SparkChessApplication {
	public static void main(String[] args) {
		new SparkController(
			new BoardDao(new ConnectionLoader()),
			new RoomDao(new ConnectionLoader()),
			new PlayerDao(new ConnectionLoader())
		).route();
	}
}
