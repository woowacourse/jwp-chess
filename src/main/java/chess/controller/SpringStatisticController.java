package chess.controller;

import chess.dto.StatusRecordDto;
import chess.entity.StatusRecordEntity;
import chess.service.ChessStatisticService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/chess/statistics")
public class SpringStatisticController {
	private final ChessStatisticService chessStatisticService;

	public SpringStatisticController(final ChessStatisticService chessStatisticService) {
		this.chessStatisticService = chessStatisticService;
	}

	@GetMapping
	public String load(final Model model) {
		final List<StatusRecordDto> statusRecord
				= chessStatisticService.loadStatusRecordsWithRoomName();

		model.addAttribute("status_record_with_room_names", statusRecord);
		return "statistics";
	}
}
