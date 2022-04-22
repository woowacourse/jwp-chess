package chess.controller.dto;

import chess.domain.Color;
import java.util.Map.Entry;

public class ChessGameScoreResponse {

	private String color;
	private double score;

	private ChessGameScoreResponse() {
	}

	private ChessGameScoreResponse(String color, double score) {
		this.color = color;
		this.score = score;
	}

	public static ChessGameScoreResponse from(Entry<Color, Double> score) {
		return new ChessGameScoreResponse(score.getKey().name(), score.getValue());
	}

	public String getColor() {
		return color;
	}

	public double getScore() {
		return score;
	}
}
