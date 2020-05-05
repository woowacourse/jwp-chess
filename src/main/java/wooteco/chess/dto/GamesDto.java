package wooteco.chess.dto;

import java.util.Collections;
import java.util.Map;

public class GamesDto {
	private final Map<Long, String> games;

	public GamesDto(Map<Long, String> games) {
		this.games = games;
	}

	public Map<Long, String> getGames() {
		return Collections.unmodifiableMap(games);
	}
}
