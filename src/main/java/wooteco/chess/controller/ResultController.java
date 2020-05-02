package wooteco.chess.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import chess.domain.GameResult;
import wooteco.chess.service.ChessGameService;

@Controller
@RequestMapping("/result")
public class ResultController {
	@Autowired
	private ChessGameService chessGameService;

	@GetMapping("/{roomID}")
	public ModelAndView winner() {
		if (chessGameService.isNotGameOver(1L)) {
			return new ModelAndView("redirect:/");
		}

		GameResult gameResult = chessGameService.findWinner(1L);
		Map<String, Object> model = createWinnerModel(gameResult);
		return new ModelAndView("winner", model);
	}

	private Map<String, Object> createWinnerModel(GameResult gameResult) {
		Map<String, Object> model = new HashMap<>();
		model.put("winner", gameResult.getWinner());
		model.put("loser", gameResult.getLoser());
		model.put("blackScore", gameResult.getAliveBlackPieceScoreSum());
		model.put("whiteScore", gameResult.getAliveWhitePieceScoreSum());

		return model;
	}
}
