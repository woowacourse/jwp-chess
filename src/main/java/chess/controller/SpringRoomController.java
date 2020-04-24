package chess.controller;

import chess.dao.exceptions.DaoNoneSelectedException;
import chess.service.ChessRoomService;
import chess.view.Announcement;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;

@Controller
public class SpringRoomController {
	private static final String PATH = "/chess/rooms";
	private static final String PATH_VARIABLE = "{room_id}";
	private static final String STATIC_PATH = "/chess";
	private static final String SLASH = "/";
	private static final String COMMAND_QUERY_PARAM = "command";
	private static final String TABLE_KEY = "table";
	private static final String ANNOUNCEMENT_KEY = "announcement";

	private final ChessRoomService chessRoomService;

	public SpringRoomController(final ChessRoomService chessRoomService) {
		this.chessRoomService = chessRoomService;
	}

	@GetMapping(PATH + SLASH + PATH_VARIABLE)
	private String load(@PathVariable("room_id") final int roomId, final Model model) throws SQLException {
		try {
			loadData(roomId, model);
		} catch (DaoNoneSelectedException e) {
			initRoom(roomId);
			loadData(roomId, model);
		}
		return STATIC_PATH;
	}

	private void loadData(final int roomId, final Model model) throws SQLException {
		final String boardHtml = chessRoomService.loadBoardHtml(roomId);
		final String announcementMessage = chessRoomService.loadAnnouncementMessage(roomId);
		model.addAttribute(TABLE_KEY, boardHtml);
		model.addAttribute(ANNOUNCEMENT_KEY, announcementMessage);
	}

	private void initRoom(final int roomId) throws SQLException {
		chessRoomService.saveNewState(roomId);
		chessRoomService.saveNewPieces(roomId);
		chessRoomService.saveNewAnnouncementMessage(roomId);
	}

	@PostMapping(PATH + SLASH + PATH_VARIABLE)
	private String command(
			@PathVariable("room_id") final int roomId,
			@RequestParam(COMMAND_QUERY_PARAM) final String command) throws SQLException {

		try {
			chessRoomService.updateRoom(roomId, command);
		} catch (RuntimeException e) {
			chessRoomService.saveAnnouncementMessage(roomId, Announcement.of(e.getMessage()).getString());
		}
		return Constants.REDIRECT + PATH + SLASH + roomId;
	}
}
