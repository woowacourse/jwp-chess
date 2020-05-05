package chess.controller;

import chess.repository.exceptions.DaoNoneSelectedException;
import chess.service.ChessRoomService;
import chess.view.Announcement;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@Controller
@RequestMapping("/chess/rooms")
public class SpringRoomController {
	private final ChessRoomService chessRoomService;

	public SpringRoomController(final ChessRoomService chessRoomService) {
		this.chessRoomService = chessRoomService;
	}

	@GetMapping("/{room_id}")
	public String load(@PathVariable("room_id") final int roomId, final Model model) {
		try {
			loadData(roomId, model);
		} catch (DaoNoneSelectedException e) {
			chessRoomService.initRoom(roomId);
			loadData(roomId, model);
		}
		return "chess";
	}

	private void loadData(final int roomId, final Model model) {
		final String boardHtml = chessRoomService.loadBoardHtml(roomId);
		final String announcementMessage = chessRoomService.loadAnnouncementMessage(roomId);
		model.addAttribute("table", boardHtml);
		model.addAttribute("announcement", announcementMessage);
	}

	@PostMapping("/{room_id}")
	public String command(
			@PathVariable("room_id") final int roomId,
			@RequestParam("command") final String command) {

		try {
			chessRoomService.updateRoom(roomId, command);
		} catch (RuntimeException e) {
			chessRoomService.saveAnnouncementMessage(roomId, Announcement.of(e.getMessage()).getString());
		}
		return Constants.REDIRECT + "/chess/rooms/" + roomId;
	}
}
