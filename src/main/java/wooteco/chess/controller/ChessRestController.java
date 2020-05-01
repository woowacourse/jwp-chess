package wooteco.chess.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.chess.dto.MoveRequestDto;
import wooteco.chess.service.ChessGameService;

@RestController
@RequestMapping("/rooms")
public class ChessRestController {
	private final ChessGameService chessGameService;

	public ChessRestController(ChessGameService chessGameService) {
		this.chessGameService = chessGameService;
	}

	@GetMapping
	public ResponseEntity getRooms() {
		return ResponseEntity.ok(chessGameService.games());
	}

	@PostMapping
	public ResponseEntity createRoom() {
		return ResponseEntity.ok(chessGameService.create());
	}

	@PutMapping("/{id}")
	public ResponseEntity restartRoom(@PathVariable Long id) {
		chessGameService.restart(id);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity deleteRoom(@PathVariable Long id) {
		chessGameService.delete(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}/board")
	public ResponseEntity getBoard(@PathVariable Long id) {
		return ResponseEntity.ok(chessGameService.find(id));
	}

	@PutMapping("/{id}/board")
	public ResponseEntity movePiece(@PathVariable Long id, @RequestBody MoveRequestDto moveRequestDto) {
		return ResponseEntity.ok(chessGameService.move(id, moveRequestDto.getSource(), moveRequestDto.getTarget()));
	}
}
