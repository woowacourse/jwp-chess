package chess.web;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import chess.service.GameService;
import chess.service.RoomService;
import chess.web.dto.BoardDto;
import chess.web.dto.RoomResponseDto;

@RestController
@RequestMapping("/api/rooms")
public class RoomApiController {

	private final RoomService roomService;
	private final GameService gameService;

	public RoomApiController(RoomService roomService, GameService gameService) {
		this.roomService = roomService;
		this.gameService = gameService;
	}

	@GetMapping
	public ResponseEntity<List<RoomResponseDto>> loadRooms() {
		List<RoomResponseDto> rooms = roomService.findAll().stream()
			.map(room -> new RoomResponseDto(room.getId(), room.getName()))
			.collect(Collectors.toList());
		return ResponseEntity.ok(rooms);
	}

	@DeleteMapping("/{roomId}")
	public ResponseEntity<?> deleteRoom(@PathVariable int roomId, @RequestParam String password) {
		roomService.delete(roomId, password);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{roomId}/start")
	public ResponseEntity<BoardDto> startNewGame(@PathVariable int roomId) {
		return ResponseEntity.ok(gameService.startNewGame(roomId));
	}

	@GetMapping("/{roomId}/load")
	public ResponseEntity<BoardDto> loadGame(@PathVariable int roomId) {
		return ResponseEntity.ok(gameService.loadGame(roomId));
	}
}