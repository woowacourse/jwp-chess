package chess.controller;

import chess.dao.exceptions.DaoNoneSelectedException;
import chess.dto.RoomDto;
import chess.service.ChessRoomsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.List;

@Controller
public class SpringRoomsController {
	public static final String PATH = "/chess/rooms";
	private static final String STATIC_PATH = "/rooms";
	private static final String EMPTY = "";
	private static final String SLASH = "/";
	private static final String ROOM_NAME_OF_FORM = "room_name";
	private static final String ROOMS_KEY = "rooms";

	private final ChessRoomsService chessRoomsService;

	public SpringRoomsController(final ChessRoomsService chessRoomsService) {
		this.chessRoomsService = chessRoomsService;
	}

	@GetMapping(PATH)
	private String load(final Model model) throws SQLException {
		final List<RoomDto> rooms = chessRoomsService.findAllRooms();
		model.addAttribute(ROOMS_KEY, rooms);
		return STATIC_PATH;
	}

	@PostMapping(PATH)
	private String manage(
			@RequestParam(value = "method", defaultValue = EMPTY) final String method,
			@RequestParam(value = ROOM_NAME_OF_FORM, defaultValue = EMPTY) final String roomName)
			throws SQLException {

		if ("delete".equals(method)) {
			return delete(roomName);
		}

		try {
			return enter(roomName);
		} catch (DaoNoneSelectedException e) {
			return create(roomName);
		}
	}

	private String delete(final String roomName) throws SQLException {
		chessRoomsService.deleteRoomByRoomName(roomName);
		return Constants.REDIRECT + PATH;
	}

	private String enter(final String roomName) throws SQLException {
		final RoomDto roomDto = chessRoomsService.findRoomByRoomName(roomName);
		return Constants.REDIRECT + PATH + SLASH + roomDto.getId();
	}

	private String create(final String roomName) throws SQLException {
		chessRoomsService.addRoomByRoomName(roomName);
		return Constants.REDIRECT + PATH;
	}
}
