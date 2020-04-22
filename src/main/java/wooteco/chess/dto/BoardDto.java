package wooteco.chess.dto;

import java.util.List;
import java.util.Map;

public class BoardDto {
	private Long gameId;
	private List<String> piece;
	private Map<String, Double> score;

	public BoardDto(Long gameId, List<String> piece, Map<String, Double> score) {
		this.gameId = gameId;
		this.piece = piece;
		this.score = score;
	}

	public Long getGameId() {
		return this.gameId;
	}

	public List<String> getPiece() {
		return this.piece;
	}

	public Map<String, Double> getScore() {
		return this.score;
	}
}
