package wooteco.chess.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import wooteco.chess.domain.Result;
import wooteco.chess.domain.chessboard.Board;
import wooteco.chess.domain.chesspiece.Piece;
import wooteco.chess.domain.position.Position;
import wooteco.chess.dto.RoomDto;
import wooteco.chess.service.ChessService;

@Controller
public class SpringController {

	private final ChessService chessService;

	public SpringController(ChessService chessService) {
		this.chessService = chessService;
	}

	@GetMapping("/")
	public String index(Model model) {
		List<RoomDto> roomDtos = Arrays.asList(new RoomDto(1L, "첫 번째 방"), new RoomDto(2L, "두 번째 방"));
		model.addAttribute("rooms", roomDtos);
		return "lobby";
	}

	@GetMapping("/init")
	@ResponseBody
	public Map<String, Object> init() {
		return makeModel(chessService.find(1L));
	}

	@PutMapping("/move")
	@ResponseBody
	public Map<String, Object> move(@RequestParam("start") String start, @RequestParam("target") String target) {
		Map<String, Object> model = new HashMap<>();

		chessService.move(1L, Position.of(start), Position.of(target));
		model.put("start", start);
		model.put("target", target);
		model.put("isEnd", chessService.isEnd(1L));
		return model;
	}

	@GetMapping("/findWinningTeam")
	@ResponseBody
	public Map<String, Object> isEnd() {
		Map<String, Object> model = new HashMap<>();
		model.put("winningTeam", chessService.findWinningTeam(1L));
		return model;
	}

	@GetMapping("/restart")
	@ResponseBody
	public Map<String, Object> restart() {
		return makeModel(chessService.restart(1L));
	}

	@GetMapping("/status")
	@ResponseBody
	public Map<String, Object> status() {
		Result result = chessService.status(1L);
		Map<String, Object> model = new HashMap<>();
		model.put("blackTeamScore", result.getBlackTeamScore());
		model.put("whiteTeamScore", result.getWhiteTeamScore());
		return model;
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler({IllegalArgumentException.class, UnsupportedOperationException.class})
	@ResponseBody
	public String handleException(RuntimeException e) {
		return e.getMessage();
	}

	private Map<String, Object> makeModel(Board board) {
		Map<String, Object> model = new HashMap<>();
		for (Piece piece : board.findAll()) {
			model.put(piece.getPosition().getString(), piece.getName());
		}
		return model;
	}
}