package wooteco.chess.dto.response;

import java.util.HashMap;
import java.util.Map;

import wooteco.chess.domain.GameResult;

public class GameResultParser {
	private final GameResult gameResult;

	public GameResultParser(GameResult gameResult) {
		this.gameResult = gameResult;
	}

	public Map<String, Object> parseModel() {
		Map<String, Object> model = new HashMap<>();

		model.put("winner", gameResult.getWinner());
		model.put("loser", gameResult.getLoser());
		model.put("blackScore", gameResult.getAliveBlackPieceScoreSum());
		model.put("whiteScore", gameResult.getAliveWhitePieceScoreSum());

		return model;
	}
}
