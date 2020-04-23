package chess.controller;

import chess.dto.StatusRecordDto;
import chess.service.ChessStatisticService;
import spark.Spark;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SparkStatisticController {
	private static final SparkStatisticController CHESS_STATISTICS_CONTROLLER;
	private static final String PATH = "/chess/statistics";
	private static final String STATIC_PATH = "/statistics.hbs";
	private static final String RECORD_KEY = "status_record_with_room_names";

	static {
		CHESS_STATISTICS_CONTROLLER = new SparkStatisticController(ChessStatisticService.getInstance());
	}

	private final ChessStatisticService chessStatisticService;

	private SparkStatisticController(final ChessStatisticService chessStatisticService) {
		this.chessStatisticService = chessStatisticService;
	}

	public static SparkStatisticController getInstance() {
		return CHESS_STATISTICS_CONTROLLER;
	}

	public void run() {
		routeGetMethod();
	}

	private void routeGetMethod() {
		Spark.get(PATH, (request, response) -> {
			final List<StatusRecordDto> statusRecordWithRoomNames
					= chessStatisticService.loadStatusRecordsWithRoomName();

			final Map<String, Object> map = new HashMap<>();
			map.put(RECORD_KEY, statusRecordWithRoomNames);
			return Renderer.getInstance().render(map, STATIC_PATH);
		});
	}
}
