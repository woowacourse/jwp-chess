package chess.controller;

import chess.model.dto.CreateRoomDto;
import chess.model.dto.DeleteRoomDto;
import chess.model.dto.RoomsDto;
import chess.service.ChessGameService;
import chess.service.RoomService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/room")
public class RoomApiController {
	private final ChessGameService chessGameService;
	private final RoomService roomService;

	public RoomApiController(ChessGameService chessGameService, RoomService roomService) {
		this.chessGameService = chessGameService;
		this.roomService = roomService;
	}

	@GetMapping
	public RoomsDto viewRooms() {
		return roomService.getUsedRooms();
	}

	@PostMapping
	public RoomsDto createRoom(@RequestBody CreateRoomDto createRoomDto) {
		roomService.addRoom(createRoomDto);
		return roomService.getUsedRooms();
	}

	@DeleteMapping
	public RoomsDto deleteRoom(@RequestBody DeleteRoomDto deleteRoomDto) {
		roomService.deleteRoom(deleteRoomDto);
		chessGameService.findProceedGameId(deleteRoomDto.getRoomId())
				.ifPresent(chessGameService::closeGame);

		return roomService.getUsedRooms();
	}
}
