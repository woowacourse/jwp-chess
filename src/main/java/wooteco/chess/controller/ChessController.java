package wooteco.chess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import wooteco.chess.service.ChessGameService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Controller
public class ChessController {
	@Autowired
	private ChessGameService chessGameService;

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/load-game")
	public ModelAndView loadChessGame() {
		Map<String, Object> model = new HashMap<>();
		try {
			model = chessGameService.loadBoard();
		} catch (NoSuchElementException e) {
			model.put("error", "새 게임을 눌러주세요!");
		}
		return new ModelAndView("index", model);
	}

	@GetMapping("/new-chess-game")
	public ModelAndView newChessGame() throws SQLException {
		Map<String, Object> model = chessGameService.createNewChessGame();
		return new ModelAndView("index", model);
	}

	@GetMapping("/winner")
	public ModelAndView winner() throws SQLException {
		if (chessGameService.isNotGameOver()) {
			return new ModelAndView("redirect:/");
		}

		Map<String, Object> model = chessGameService.findWinner();
		return new ModelAndView("winner", model);
	}

	@PostMapping("/move")
	public ModelAndView winner(@RequestParam(defaultValue = "") String source,
		@RequestParam(defaultValue = "") String target) throws SQLException {
		Map<String, Object> model;
		try {
			model = chessGameService.movePiece(source, target);
		} catch (IllegalArgumentException e) {
			model = chessGameService.loadBoard();
			model.put("error", e.getMessage());
		}

		if (chessGameService.isGameOver()) {
			return new ModelAndView("redirect:/winner");
		}
		return new ModelAndView("index", model);
	}
}
