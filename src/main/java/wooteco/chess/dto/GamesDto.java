package wooteco.chess.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public class GamesDto {
	private Map<Long, String> games = new LinkedHashMap<>();

	public GamesDto(Map<Long, String> games) {
		this.games = games;
	}

	public Map<Long, String> getGames() {
		return games;
	}
}
