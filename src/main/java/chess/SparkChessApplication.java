package chess;

import chess.controller.SparkHomeController;
import chess.controller.SparkRoomController;
import chess.controller.SparkRoomsController;
import chess.controller.SparkStatisticController;
import spark.Spark;

public class SparkChessApplication {

	private static final SparkRoomsController ROOMS_CONTROLLER = SparkRoomsController.getInstance();
	private static final SparkRoomController ROOM_CONTROLLER = SparkRoomController.getInstance();
	private static final SparkStatisticController STATISTIC_CONTROLLER
			= SparkStatisticController.getInstance();
	private static final SparkHomeController HOME_CONTROLLER = SparkHomeController.getInstance();

	public static void main(String[] args) {
		Spark.port(ServerInfo.PORT);
		Spark.staticFiles.location(ServerInfo.STATIC_FILES);

		ROOMS_CONTROLLER.run();
		ROOM_CONTROLLER.run();
		STATISTIC_CONTROLLER.run();
		HOME_CONTROLLER.run();

	}
}
