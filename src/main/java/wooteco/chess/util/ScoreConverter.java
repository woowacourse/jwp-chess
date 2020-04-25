package wooteco.chess.util;

import java.util.Map;
import java.util.stream.Collectors;

import wooteco.chess.domain.piece.team.Team;

public class ScoreConverter {
	public static Map<String, Double> convert(Map<Team, Double> inputScore) {
		return inputScore.entrySet().stream()
			.collect(Collectors.toMap(k -> k.getKey().getName(), Map.Entry::getValue));
	}
}
