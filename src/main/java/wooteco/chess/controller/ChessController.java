package wooteco.chess.controller;

import chess.domain.GameResult;
import chess.domain.board.ChessBoard;
import chess.dto.CellManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import wooteco.chess.service.ChessGameService;

import java.util.HashMap;
import java.util.Map;

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
		ChessBoard chessBoard = chessGameService.createNewChessGame(roomId);
		Map<String, Object> model = createCurrentGameModel(chessBoard, roomId);
		return new ModelAndView("game", model);
	}

	@GetMapping("/{roomId}/load")
	public ModelAndView loadChessGame(@PathVariable Long roomId) {
		Map<String, Object> model = new HashMap<>();
		try {
			model = createCurrentGameModel(chessGameService.loadBoard(roomId), roomId);
		} catch (IllegalArgumentException e) {
			model.put("error", e.getMessage());
		}
		return new ModelAndView("game", model);
	}

	@PostMapping("/{roomId}/move")
	public ModelAndView winner(@PathVariable Long roomId,
		@RequestParam(defaultValue = "") String source,
		@RequestParam(defaultValue = "") String target) {

		ChessBoard chessBoard;
		Map<String, Object> model;
		try {
			chessBoard = chessGameService.movePiece(roomId, source, target);
			model = createCurrentGameModel(chessBoard, roomId);
		} catch (IllegalArgumentException e) {
			chessBoard = chessGameService.loadBoard(roomId);
			model = createCurrentGameModel(chessBoard, roomId);
			model.put("error", e.getMessage());
		}

		if (chessBoard.isGameOver()) {
			return new ModelAndView(String.format("redirect:/result/%d", roomId));
		}
		return new ModelAndView("game", model);
	}

	private Map<String, Object> createCurrentGameModel(ChessBoard chessBoard, Long roomId) {
		Map<String, Object> model = new HashMap<>();
		GameResult gameResult = chessBoard.createGameResult();
		CellManager cellManager = new CellManager();

		model.put("cells", cellManager.createCells(chessBoard));
		model.put("currentTeam", chessBoard.getTeam().getName());
		model.put("blackScore", gameResult.getAliveBlackPieceScoreSum());
		model.put("whiteScore", gameResult.getAliveWhitePieceScoreSum());
		model.put("roomId", roomId);

		return model;
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ModelAndView exceptionHandler(Exception e) {
		Map<String, Object> model = new HashMap<>();
		model.put("error", e.getMessage());
		return new ModelAndView("error", model);
	}
}
