package chess.controller;

import chess.dto.StatusRecordDto;
import chess.service.ChessStatisticService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.SQLException;
import java.util.List;

@Controller
public class SpringStatisticController {
	private static final String PATH = "/chess/statistics";
	private static final String STATIC_PATH = "/statistics";
	private static final String RECORD_KEY = "status_record_with_room_names";

	private final ChessStatisticService chessStatisticService;

	public SpringStatisticController(final ChessStatisticService chessStatisticService) {
		this.chessStatisticService = chessStatisticService;
	}

	@GetMapping(PATH)
	private String load(final Model model) throws SQLException {
			final List<StatusRecordDto> statusRecordWithRoomNames
					= chessStatisticService.loadStatusRecordsWithRoomName();

			model.addAttribute(RECORD_KEY, statusRecordWithRoomNames);
			return STATIC_PATH;
	}
}
