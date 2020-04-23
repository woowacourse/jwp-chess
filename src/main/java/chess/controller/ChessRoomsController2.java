package chess.controller;

import chess.dao.exceptions.DaoNoneSelectedException;
import chess.dto.RoomDto;
import chess.service.ChessRoomsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spark.Response;
import spark.Spark;

import java.sql.SQLException;
import java.util.List;

@Controller
public class ChessRoomsController2 {
	public static final String PATH = "/chess/rooms";
	private static final String STATIC_PATH = "/rooms";
	private static final String EMPTY = "";
	private static final String SLASH = "/";
	private static final String ROOM_NAME_OF_FORM = "room_name";
	private static final String ROOMS_KEY = "rooms";

	private final ChessRoomsService chessRoomsService;

	public ChessRoomsController2(final ChessRoomsService chessRoomsService) {
		this.chessRoomsService = chessRoomsService;
	}

	@GetMapping(PATH)
	private String roomList(final Model model) throws SQLException {
		final List<RoomDto> rooms = chessRoomsService.findAllRooms();
		model.addAttribute(ROOMS_KEY, rooms);
		return STATIC_PATH;
	}

	@PostMapping(PATH)
	private String routePostMethod(
			@RequestParam(value = "method", defaultValue = EMPTY) final String method,
			@RequestParam(value = ROOM_NAME_OF_FORM, defaultValue = EMPTY) final String roomName)
			throws SQLException {

		if ("delete".equals(method)) {
			chessRoomsService.deleteRoomByRoomName(roomName);
			return "redirect:" + PATH;
		}

		try {
			final RoomDto roomDto = chessRoomsService.findRoomByRoomName(roomName);
			return "redirect:" + PATH + SLASH + roomDto.getId();
		} catch (DaoNoneSelectedException e) {
			chessRoomsService.addRoomByRoomName(roomName);
			return "redirect:" + PATH;
		}
	}
}
