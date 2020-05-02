package wooteco.chess.controller;

import chess.domain.GameResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import wooteco.chess.service.ChessGameService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/result")
public class ResultController {
	@Autowired
	private ChessGameService chessGameService;

    @GetMapping("/{roomID}")
    public ModelAndView winner(@PathVariable Long roomID) {
        if (chessGameService.isNotGameOver(roomID)) {
            return new ModelAndView(String.format("redirect:/chess/%d", roomID));
        }

        GameResult gameResult = chessGameService.findWinner(roomID);
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

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView exceptionHandler(Exception e) {
        Map<String, Object> model = new HashMap<>();
        model.put("error", e.getMessage());
        return new ModelAndView("error", model);
    }
}
