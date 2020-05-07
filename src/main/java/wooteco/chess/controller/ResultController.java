package wooteco.chess.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import wooteco.chess.dto.response.GameResultParser;
import wooteco.chess.service.ChessGameService;

@Controller
@RequestMapping("/result")
public class ResultController {
	private final ChessGameService chessGameService;

	public ResultController(ChessGameService chessGameService) {
		this.chessGameService = chessGameService;
	}

	@GetMapping("/{roomID}")
	public ModelAndView winner(@PathVariable Long roomID) {
		if (chessGameService.isNotGameOver(roomID)) {
			return new ModelAndView(String.format("redirect:/chess/%d", roomID));
		}
		GameResultParser gameResultParser = chessGameService.findWinner(roomID);
		Map<String, Object> model = gameResultParser.parseModel();
		return new ModelAndView("winner", model);
	}
}
