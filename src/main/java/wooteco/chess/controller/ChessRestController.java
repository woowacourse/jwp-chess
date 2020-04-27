package wooteco.chess.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import wooteco.chess.dto.MoveRequestDto;
import wooteco.chess.dto.ResponseDto;
import wooteco.chess.service.ChessGameService;

@RequestMapping("/rooms")
@RestController
public class ChessRestController {
	private final ChessGameService chessGameService;

	public ChessRestController(ChessGameService chessGameService) {
		this.chessGameService = chessGameService;
	}

	@GetMapping
	public ResponseDto getRooms() throws Exception {
		return chessGameService.games();
	}

	@PostMapping
	public ResponseDto createRoom() throws Exception {
		return chessGameService.create();
	}

	@PutMapping("/{id}")
	public ResponseDto restartRoom(@PathVariable Integer id) throws Exception {
		return chessGameService.restart(id);
	}

	@DeleteMapping("/{id}")
	public ResponseDto deleteRoom(@PathVariable Integer id) throws Exception {
		return chessGameService.delete(id);
	}

	@GetMapping("/{id}/board")
	public ResponseDto getBoard(@PathVariable Integer id) throws Exception {
		return chessGameService.find(id);
	}

	@PutMapping("/{id}/board")
	public ResponseDto movePiece(@PathVariable Integer id, @ModelAttribute MoveRequestDto moveRequestDto) throws
			Exception {
		return chessGameService.move(id, moveRequestDto.source(), moveRequestDto.target());
	}
}
