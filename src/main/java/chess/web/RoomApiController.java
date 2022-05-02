package chess.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import chess.service.ChessGameService;
import chess.web.dto.BoardDto;
import chess.web.dto.RoomResponseDto;

@RestController
@RequestMapping("/api/rooms")
public class RoomApiController {

	private final ChessGameService chessGameService;

	public RoomApiController(ChessGameService chessGameService) {
		this.chessGameService = chessGameService;
	}

	@GetMapping
	public ResponseEntity<List<RoomResponseDto>> loadRooms() {
		return ResponseEntity.ok(chessGameService.findAllResponseDto());
	}

	@DeleteMapping("/{roomId}")
	public ResponseEntity<?> deleteRoom(@PathVariable int roomId, @RequestParam String password) {
		chessGameService.delete(roomId, password);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{roomId}")
	public ResponseEntity<BoardDto> loadGame(@PathVariable int roomId) {
		return ResponseEntity.ok(chessGameService.getBoardDtoByGameId(roomId));
	}
}
