package wooteco.chess.entity;

import org.springframework.data.relational.core.mapping.Table;

@Table("history_game")
public class GameRef {
	private Long game;

	public GameRef() {
	}

	public GameRef(Game game) {
		this.game = game.getId();
	}
}
