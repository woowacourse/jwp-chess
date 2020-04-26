package wooteco.chess.controller.spring;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import wooteco.chess.domain.position.Position;
import wooteco.chess.dto.GameDto;
import wooteco.chess.dto.req.MoveRequestDto;
import wooteco.chess.service.BoardService;

@Controller
public class BoardController {
	private final BoardService boardService;

	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}

	@GetMapping("/loadBoard/{id}")
	@ResponseBody
	public ResponseEntity<Object> loadBoard(@PathVariable int id) {
		try {
			return ResponseEntity.status(200).body(boardService.load(id));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	@PostMapping("/move/{id}")
	public ResponseEntity<Object> move(@PathVariable int id, @RequestBody MoveRequestDto req) {
		try {
			return ResponseEntity
				.status(200)
				.body(boardService.move(
					id,
					Position.of(req.getSourceX(), req.getSourceY()),
					Position.of(req.getTargetX(), req.getTargetY())));
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return ResponseEntity.status(406).body(e.getMessage());
		}
	}
}
