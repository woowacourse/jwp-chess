package wooteco.chess.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import wooteco.chess.domain.Result;
import wooteco.chess.domain.chessboard.Board;
import wooteco.chess.domain.chesspiece.Piece;
import wooteco.chess.domain.position.Position;
import wooteco.chess.entity.BoardEntity;
import wooteco.chess.service.ChessService;

@Controller
public class SpringController {
	private final ChessService chessService;

	public SpringController(ChessService chessService) {
		this.chessService = chessService;
	}

	@GetMapping("/")
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView();
		List<BoardEntity> roomIds = chessService.findRoomIds();
		modelAndView.setViewName("index");
		modelAndView.addObject("roomIds", roomIds);
		return modelAndView;
	}

	@GetMapping("/chess/{roomId}")
	public String index2(@PathVariable String roomId) {
		chessService.init(Long.parseLong(roomId));
		return "chess";
	}

	@GetMapping("/exit")
	public ModelAndView roomExit() {
		return index();
	}

	@GetMapping("/init")
	@ResponseBody
	public Map<String, Object> init(@RequestParam("roomId") Long roomId) {
		return makeModel(chessService.init(roomId));
	}

	@PutMapping("/move")
	@ResponseBody
	public Map<String, Object> move(@RequestParam("roomId") Long roomId, @RequestParam("start") String start,
		@RequestParam("target") String target) {
		Board board = chessService.move(Position.of(start), Position.of(target), roomId);
		return makeModel(board);
	}

	@GetMapping("/add")
	@ResponseBody
	public String addRoom(@RequestParam("roomId") Long roomId) {
		chessService.init(roomId);
		return index2(roomId + "");
	}

	@GetMapping("/isEnd")
	@ResponseBody
	public Map<String, Object> isEnd(@RequestParam("roomId") Long roomId) {
		Map<String, Object> model = new HashMap<>();
		if (chessService.isNotEnd(roomId)) {
			model.put("isEnd", false);
			return model;
		}
		model.put("isEnd", true);
		model.put("winningTeam", chessService.findWinningTeam(roomId));
		return model;
	}

	@GetMapping("/restart")
	@ResponseBody
	public Map<String, Object> restart(@RequestParam("roomId") Long roomId) {
		return makeModel(chessService.restart(roomId));
	}

	@GetMapping("/status")
	@ResponseBody
	public Map<String, Object> status(@RequestParam("roomId") Long roomId) {
		Result result = chessService.status(roomId);
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
			model.put(piece.getPosition(), piece.getName());
		}
		return model;
	}
}
