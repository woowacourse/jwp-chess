package wooteco.chess.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import wooteco.chess.dto.ChessGameParser;
import wooteco.chess.service.ChessGameService;

@Controller
@RequestMapping("/chess")
public class ChessController {
	@Autowired
	private ChessGameService chessGameService;

	@GetMapping("/{roomId}")
	public ModelAndView enterRoom(@PathVariable String roomId) {
		Map<String, Object> model = new HashMap<>();
		model.put("roomId", roomId);
		return new ModelAndView("game", model);
	}

	@PostMapping("/{roomId}")
	public ModelAndView newChessGame(@PathVariable Long roomId) {
		ChessGameParser chessGameParser = chessGameService.createNewChessGame(roomId);
		Map<String, Object> model = chessGameParser.parseModel();
		return new ModelAndView("game", model);
	}

	@GetMapping("/{roomId}/load")
	public ModelAndView loadChessGame(@PathVariable Long roomId) {
		Map<String, Object> model = new HashMap<>();
		try {
			model = chessGameService.loadBoard(roomId).parseModel();
		} catch (IllegalArgumentException e) {
			model.put("error", e.getMessage());
		}
		return new ModelAndView("game", model);
	}

	@PostMapping("/{roomId}/move")
	public ModelAndView winner(@PathVariable Long roomId,
		@RequestParam(defaultValue = "") String source,
		@RequestParam(defaultValue = "") String target) {

		Map<String, Object> model;
		try {
			model = chessGameService.movePiece(roomId, source, target).parseModel();
		} catch (IllegalArgumentException e) {
			model = chessGameService.loadBoard(roomId).parseModel();
			model.put("error", e.getMessage());
		}

		if (chessGameService.isGameOver(roomId)) {
			return new ModelAndView(String.format("redirect:/result/%d", roomId));
		}
		return new ModelAndView("game", model);
	}
}
