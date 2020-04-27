package chess;

import chess.controller.SparkHomeController;
import chess.controller.SparkRoomController;
import chess.controller.SparkRoomsController;
import chess.controller.SparkStatisticController;
import chess.dao.*;
import chess.service.ChessRoomService;
import chess.service.ChessRoomsService;
import chess.service.ChessStatisticService;
import spark.Spark;

public class SparkChessApplication {

	private static final ConnectionDao connectionDao = new ConnectionDao();
	private static final SparkRoomsController ROOMS_CONTROLLER
			= new SparkRoomsController(new ChessRoomsService(new RoomDao(connectionDao)));
	private static final SparkRoomController ROOM_CONTROLLER
			= new SparkRoomController(new ChessRoomService(
			new StateDao(connectionDao),
			new PieceDao(connectionDao),
			new StatusRecordDao(connectionDao),
			new AnnouncementDao(connectionDao)));
	private static final SparkStatisticController STATISTIC_CONTROLLER
			= new SparkStatisticController(new ChessStatisticService(new StatusRecordDao(connectionDao)));
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
