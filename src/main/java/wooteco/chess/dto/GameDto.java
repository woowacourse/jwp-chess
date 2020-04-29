package wooteco.chess.dto;

import wooteco.chess.domain.entity.Game;

public class GameDto {
	private Long id;
	private String turn;

	private GameDto(Long id, String turn) {
		this.id = id;
		this.turn = turn;
	}

	public GameDto(Game game) {
		this(game.getId(), game.getTurn());
	}

	public String getTurn() {
		return turn;
	}

	public Long getId() {
		return id;
	}
}
