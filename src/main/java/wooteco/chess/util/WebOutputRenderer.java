package wooteco.chess.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import wooteco.chess.domain.piece.Color;
import wooteco.chess.dto.ScoreDto;

public class WebOutputRenderer {
	public static List<ScoreDto> scoreToModel(Map<Color, Double> scores) {
		return scores.entrySet()
			.stream()
			.map(x -> new ScoreDto(x.getKey(), x.getValue()))
			.collect(Collectors.toList());
	}
}
