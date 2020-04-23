package chess;

import chess.controller.SparkChessHomeController;
import chess.controller.SparkChessRoomController;
import chess.controller.SparkChessRoomsController;
import chess.controller.SparkChessStatisticController;
import spark.Spark;

public class SparkChessApplication {

	private static final SparkChessRoomsController ROOMS_CONTROLLER = SparkChessRoomsController.getInstance();
	private static final SparkChessRoomController ROOM_CONTROLLER = SparkChessRoomController.getInstance();
	private static final SparkChessStatisticController STATISTIC_CONTROLLER
			= SparkChessStatisticController.getInstance();
	private static final SparkChessHomeController HOME_CONTROLLER = SparkChessHomeController.getInstance();

	public static void main(String[] args) {
		Spark.port(ServerInfo.PORT);
		Spark.staticFiles.location(ServerInfo.STATIC_FILES);

		ROOMS_CONTROLLER.run();
		ROOM_CONTROLLER.run();
		STATISTIC_CONTROLLER.run();
		HOME_CONTROLLER.run();

	}
}
